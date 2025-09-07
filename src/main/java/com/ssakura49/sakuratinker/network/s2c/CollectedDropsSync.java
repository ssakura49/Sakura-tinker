package com.ssakura49.sakuratinker.network.s2c;

import com.ssakura49.sakuratinker.common.entity.YoyoEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collections;
import java.util.function.Supplier;

public class CollectedDropsSync {

    private final int yoyoID;
    private final ItemStack[] drops;


    public CollectedDropsSync(YoyoEntity yoyo) {
        this.yoyoID = yoyo.getId();
        this.drops = yoyo.getCollectedDrops().toArray(new ItemStack[]{});
    }

    public CollectedDropsSync(FriendlyByteBuf buf){
        this.yoyoID = buf.readVarInt();
        int size = buf.readVarInt();

        drops = new ItemStack[size];

        for (int i = 0; i < size; i++) {
            drops[i] = readBigItemStack(buf);
        }
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeVarInt(yoyoID);
        buf.writeVarInt(drops.length);
        for (ItemStack drop : drops) {
            writeBigItemStack(buf,drop);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()->{
            Entity e = null;
            if (Minecraft.getInstance().level != null) {
                e = Minecraft.getInstance().level.getEntity(yoyoID);
            }
            if (e instanceof YoyoEntity yoyo){
                yoyo.getCollectedDrops().clear();
                Collections.addAll(yoyo.getCollectedDrops(),drops);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private void writeBigItemStack(FriendlyByteBuf buf, ItemStack stack) {
        if (stack.isEmpty()) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            Item item = stack.getItem();
            buf.writeVarInt(Item.getId(item));
            buf.writeVarInt(stack.getCount());
            CompoundTag tag = new CompoundTag();

            if (item.isDamageable(stack) || item.shouldOverrideMultiplayerNbt()) {
                tag = stack.getShareTag();
            }
            buf.writeNbt(tag);
        }
    }

    private ItemStack readBigItemStack(FriendlyByteBuf buf) {
        if (!buf.readBoolean()) {
            return ItemStack.EMPTY;
        } else {
            int id = buf.readVarInt();
            int count = buf.readVarInt();
            ItemStack itemstack = new ItemStack(Item.byId(id), count);
            itemstack.readShareTag(buf.readNbt());
            return itemstack;
        }
    }
}
