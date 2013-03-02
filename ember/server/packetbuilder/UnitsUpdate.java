package packetbuilder;

import ember.engine.Constants;
import ember.server.game.Player;

/**
 * 
 */
public class UnitsUpdate implements PacketBuilder {

    /**
     * Prevent instance creation.
     */
    private UnitsUpdate() {
    }

    /**
     * Update the specified player's NPCs.
     *
     * @param p
     */
    public static void update(Player p) {

        StaticPacketBuilder mainPacket = new StaticPacketBuilder().setId(250).setSize(Size.VAR_SHORT).initBitAccess();
        StaticPacketBuilder updateBlock = new StaticPacketBuilder().setBare(true);
        mainPacket.addBits(8, p.getNpcListSize());
        int size = p.getNpcListSize();
        p.setNpcListSize(0);
        boolean[] newNpc = new boolean[Constants.NPC_CAP];
        for (int i = 0; i < size; i++) {
            if (p.getNpcList()[i] == null || !p.getNpcList()[i].getLocation().withinDistance(p.getLocation()) || p.getUpdateFlags().didTeleport() || p.getNpcList()[i].isHidden()) {
                if (p.getNpcList()[i] != null) {
                    p.getNpcsInList()[p.getNpcList()[i].getIndex()] = 0;
                    	//p.getNpcList()[i] = null;
                }
                mainPacket.addBits(1, 1);
                mainPacket.addBits(2, 3);
            } else {
                updateNpcMovement(p.getNpcList()[i], mainPacket);
                if (p.getNpcList()[i].getUpdateFlags().isUpdateRequired()) {
                    appendUpdateBlock(p.getNpcList()[i], updateBlock);
                }
                p.getNpcList()[p.getNpcListSize()] = p.getNpcList()[i];
                p.setNpcListSize(p.getNpcListSize() + 1);
            }
        }
        for (AnimalIntelligence npc : World.getInstance().getNpcList()) {
            if (npc == null || p.getNpcsInList()[npc.getIndex()] == 1 || !npc.getLocation().withinDistance(p.getLocation()) || npc.isHidden()) {
                continue;
            }
            addNewNpc(p, npc, mainPacket);
            if (npc.getUpdateFlags().isUpdateRequired()) {
                appendUpdateBlock(npc, updateBlock);
            }
            newNpc[npc.getIndex()] = true;
        }
        if (updateBlock.getLength() >= 3) {
            mainPacket.addBits(15, 32767);
        }
        mainPacket.finishBitAccess();
        if (updateBlock.getLength() > 0) {
            mainPacket.addBytes(updateBlock.toPacket().getData());
        }
        p.getSession().writePacket(mainPacket.toPacket());
    }
    private static void addNewNpc(Player p, AnimalIntelligence npc, StaticPacketBuilder mainPacket) {
        p.getNpcsInList()[npc.getIndex()] = 1;
        p.getNpcList()[p.getNpcListSize()] = npc;
        p.setNpcListSize(p.getNpcListSize() + 1);
        int y = npc.getLocation().getY() - p.getLocation().getY();
        if (y < 0) {
            y += 32;
        }
        int x = npc.getLocation().getX() - p.getLocation().getX();
        if (x < 0) {
            x += 32;
        }
        mainPacket.addBits(15, npc.getIndex());
	mainPacket.addBits(1, 1);
	mainPacket.addBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
	mainPacket.addBits(5, y);
        mainPacket.addBits(14, npc.getId());
	mainPacket.addBits(3, 0);
        mainPacket.addBits(5, x);
    }

    private static void updateNpcMovement(AnimalIntelligence npc, StaticPacketBuilder mainPacket) {
        int sprite = npc.getSprite();
        if (sprite == -1) {
            if (npc.getUpdateFlags().isUpdateRequired()) {
                mainPacket.addBits(1, 1);
                mainPacket.addBits(2, 0);
            } else {
                mainPacket.addBits(1, 0);
            }
        } else {
            mainPacket.addBits(1, 1);
            mainPacket.addBits(2, 1);
            int dir = Constants.XLATE_DIRECTION_TO_CLIENT[sprite];
            mainPacket.addBits(3, dir);
            mainPacket.addBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
        }
    }

    private static void appendUpdateBlock(AnimalIntelligence npc, StaticPacketBuilder updateBlock) {
		int mask = 0x0;
		if(npc.getUpdateFlags().isGraphicsUpdateRequired()) {
			mask |= 0x10;
		}
		if(npc.getUpdateFlags().isHit2UpdateRequired()) {
			mask |= 0x2;
		}
		if(npc.getUpdateFlags().isAnimationUpdateRequired()) {
			mask |= 0x80;
		}
		if(npc.getUpdateFlags().isFaceToUpdateRequired()) {
			mask |= 0x1;
		}
		if(npc.getUpdateFlags().isHit1UpdateRequired()) {
			mask |= 0x8;
		}
		updateBlock.addByte((byte) mask);
		if(npc.getUpdateFlags().isGraphicsUpdateRequired()) {
			appendGraphicsUpdate(npc, updateBlock);
		}
		if(npc.getUpdateFlags().isHit2UpdateRequired()) {
			appendHit2Update(npc, updateBlock);
		}
		if(npc.getUpdateFlags().isAnimationUpdateRequired()) {
			appendAnimationUpdate(npc, updateBlock);
		}
		if(npc.getUpdateFlags().isFaceToUpdateRequired()) {
			appendFaceToUpdate(npc, updateBlock);
		}
		if(npc.getUpdateFlags().isHit1UpdateRequired()) {
			appendHit1Update(npc, updateBlock);
		}
    }

    private static void appendHit1Update(AnimalIntelligence npc, StaticPacketBuilder updateBlock) {
        updateBlock.addSmart(npc.getHits().getHitDamage1());
        updateBlock.addByte((byte) npc.getHits().getHitType1());
        int hpRatio = npc.getHitpoints() * 255 / npc.getDefinition().getHitpoints();
        updateBlock.addByte((byte) hpRatio);
    }

    private static void appendHit2Update(AnimalIntelligence npc, StaticPacketBuilder updateBlock) {
        updateBlock.addSmart(npc.getHits().getHitDamage2());
        updateBlock.addByteC(npc.getHits().getHitType2());
    }

    private static void appendGraphicsUpdate(AnimalIntelligence npc, StaticPacketBuilder updateBlock) {
        updateBlock.addLEShort(npc.getLastGraphics().getId());
        updateBlock.addLEInt(npc.getLastGraphics().getDelay());
    }

    private static void appendAnimationUpdate(AnimalIntelligence npc, StaticPacketBuilder updateBlock) {
        updateBlock.addLEShortA(npc.getLastAnimation().getId());
        updateBlock.addByteA(npc.getLastAnimation().getDelay());
    }

    private static void appendFaceToUpdate(AnimalIntelligence npc, StaticPacketBuilder updateBlock) {
        updateBlock.addLEShort(npc.getUpdateFlags().getFaceTo());
	}

}
