package com.bsixel.mysticism.common.api.ability;

import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.math.IPositionableTreeNode;
import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import com.bsixel.mysticism.common.api.spells.SpellHelper;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Ability implements IPositionableTreeNode<Ability> {

    private static final Map<ResourceLocation, Ability> REGISTRY = new HashMap<>(); // TODO: Probably do this differently, idk. Really have no idea how best to do this

    private final ResourceLocation id; // Unsure exactly how we want to use this, but these should definitely be at least partially data driven
    private final LinkedList<Ability> children = Lists.newLinkedList();
    private final Ability parent; // May be null if this is the root of a tree
    private Ability leftNeighbor; // May be null if this is the root of a tree
    private Ability rightNeighbor; // May be null if this is the last leaf of a tree
    private Ability prevNode; // etc
    private Ability nextLevel;
    private int childIndex;
    private int depth;
    private final Force force;
    private final int maxLevel;
    private final int cost;
    private final ItemStack icon;
    private final ITextComponent name;
    private final ITextComponent description;
    private int xPos;
    private int modifier;
    private int prelimX;
    private int yPos;
    private final ISpellComponent unlockedComponent; // May be null if we instead get something else, like an effect. TODO: Still unsure how exactly I want to do this

    public Ability(ResourceLocation id, Ability parent, ItemStack icon, Force force, int maxLevel, int cost, ITextComponent name, ITextComponent description, ISpellComponent unlockedComponent) {
        this.id = id;
        this.parent = parent;
        this.childIndex = parent != null ? parent.getChildren().size() : 0; // Zero if this is a root node
        this.depth = parent != null ? parent.depth + 1 : 0; // Zero if this is a root node
        this.icon = icon;
        this.force = force;
        this.maxLevel = maxLevel;
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.unlockedComponent = unlockedComponent;
    }

    public static void register(Ability ability) {
        REGISTRY.put(ability.id, ability);
    }

    public static Ability getRegisteredAbility(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    @Override
    public void setPos(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public int getX() {
        return this.xPos;
    }

    @Override
    public int getY() {
        return this.yPos;
    }

    @Override
    public Vector2f getPos() {
        return new Vector2f(this.xPos, this.yPos);
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getIcon() {
        return this.icon;
    }

    public JsonElement serialize() {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("id", this.id.toString());
        jsonobject.addProperty("force", this.force.name());
        jsonobject.add("icon", this.serializeIcon());
        jsonobject.addProperty("maxLevel", this.maxLevel);
        jsonobject.addProperty("cost", this.cost);
        jsonobject.add("name", ITextComponent.Serializer.toJsonTree(this.name));
        jsonobject.add("description", ITextComponent.Serializer.toJsonTree(this.description));
        jsonobject.addProperty("component", this.unlockedComponent.getClass().getCanonicalName());

        return jsonobject;
    }

    public static Ability deserialize(JsonObject object) {
        ResourceLocation idLocation = object.has("id") ? new ResourceLocation(JSONUtils.getString(object, "id")) : null;
        if (idLocation != null) {
            Ability parent = REGISTRY.get(new ResourceLocation(JSONUtils.getString(object, "parent_id")));
            Force force = Force.valueOf(JSONUtils.getString(object, "force"));
            ItemStack itemstack = deserializeIcon(JSONUtils.getJsonObject(object, "icon"));
            int maxLevel = JSONUtils.getInt(object, "maxLevel", 1);
            int cost = JSONUtils.getInt(object, "cost", 1);
            ITextComponent name = ITextComponent.Serializer.getComponentFromJson(object.get("name"));
            ITextComponent description = ITextComponent.Serializer.getComponentFromJson(object.get("description"));
            String componentClass = JSONUtils.getString(object, "component");
            return new Ability(idLocation, parent, itemstack, force, maxLevel, cost, name, description, SpellHelper.getRegisteredComponent(componentClass));
        } else {
            throw new JsonSyntaxException("Both title and description must be set");
        }
    }

    private JsonObject serializeIcon() {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("item", Registry.ITEM.getKey(this.icon.getItem()).toString());
        if (this.icon.hasTag()) {
            jsonobject.addProperty("nbt", this.icon.getTag().toString());
        }

        return jsonobject;
    }

    private static ItemStack deserializeIcon(JsonObject object) {
        if (!object.has("item")) {
            throw new JsonSyntaxException("Unsupported icon type, currently only items are supported (add 'item' key)");
        } else {
            Item item = JSONUtils.getItem(object, "item");
            if (object.has("data")) {
                throw new JsonParseException("Disallowed data tag found");
            } else {
                ItemStack itemstack = new ItemStack(item);
                if (object.has("nbt")) {
                    try {
                        CompoundNBT compoundnbt = JsonToNBT.getTagFromJson(JSONUtils.getString(object.get("nbt"), "nbt"));
                        itemstack.setTag(compoundnbt);
                    } catch (CommandSyntaxException commandsyntaxexception) {
                        throw new JsonSyntaxException("Invalid nbt tag: " + commandsyntaxexception.getMessage());
                    }
                }

                return itemstack;
            }
        }
    }

    public void write(PacketBuffer buf) {
        buf.writeTextComponent(this.name);
        buf.writeTextComponent(this.description);

        buf.writeFloat(this.xPos);
        buf.writeFloat(this.yPos);
    }

    public static DisplayInfo read(PacketBuffer buf) {
        ITextComponent itextcomponent = buf.readTextComponent();
        ITextComponent itextcomponent1 = buf.readTextComponent();
        ItemStack itemstack = buf.readItemStack();
        FrameType frametype = buf.readEnumValue(FrameType.class);
        int i = buf.readInt();
        ResourceLocation resourcelocation = (i & 1) != 0 ? buf.readResourceLocation() : null;
        boolean flag = (i & 2) != 0;
        boolean flag1 = (i & 4) != 0;
        DisplayInfo displayinfo = new DisplayInfo(itemstack, itextcomponent, itextcomponent1, resourcelocation, frametype, flag, false, flag1);
        displayinfo.setPosition(buf.readFloat(), buf.readFloat());
        return displayinfo;
    }

    public Force getForce() {
        return force;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getCost() {
        return cost;
    }

    public ITextComponent getName() {
        return name;
    }

    public ITextComponent getDescription() {
        return description;
    }

    @Override
    public LinkedList<Ability> getChildren() {
        return children;
    }

    @Override
    public Ability getParent() {
        return this.parent;
    }

    @Override
    public Ability setLeftNeighbor(Ability node) {
        this.leftNeighbor = node;
        return this;
    }

    @Override
    public Ability setRightNeighbor(Ability node) {
        this.rightNeighbor = node;
        return this;
    }

    @Override
    public Ability getLeftNeighbor() {
        return this.leftNeighbor;
    }

    @Override
    public Ability getRightNeighbor() {
        return this.rightNeighbor;
    }

    @Override
    public Ability setPrevNode(Ability prevNode) {
        this.prevNode = prevNode;
        return this;
    }

    @Override
    public Ability getPrevNode() {
        return this.prevNode;
    }

    @Override
    public Ability setNextLevel(Ability nextLevel) {
        this.nextLevel = nextLevel;
        return this;
    }

    @Override
    public Ability getNextLevel() {
        return this.nextLevel;
    }

    @Override
    public int childIndex() {
        return this.childIndex;
    }

    @Override
    public void setChildIndex(int index) {
        this.childIndex = index;
    }

    @Override
    public int depth() {
        return this.depth;
    }

    @Override
    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    @Override
    public int getModifier() {
        return this.modifier;
    }

    @Override
    public void setPrelimX(int x) {
        this.prelimX = x;
    }

    @Override
    public int getPrelimX() {
        return this.prelimX;
    }

    /**
     * Please use Tree.addNode instead
     * @param child
     */
    @Override
    @Deprecated
    public void addChild(Ability child) {
        int childrenSize = this.getChildren().size();
        if (childrenSize > 0) {
            child.setLeftNeighbor(this.getChildren().get(childrenSize-1));
        } else {
            child.setLeftNeighbor(null);
        }
        child.setRightNeighbor(null);
        this.children.add(child);
    }

    @Override
    public Ability firstChild() {
        return this.children.size() > 0 ? this.children.get(0) : null;
    }

    public ISpellComponent getUnlockedComponent() {
        return unlockedComponent;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Ability)) {
            return false;
        }
        return ((Ability) obj).name.equals(this.name);
    }
}
