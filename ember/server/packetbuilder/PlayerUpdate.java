package packetbuilder;

import ember.server.game.Player;


/**
 * Player update block.
 *
 * @author Graham
 */
public class PlayerUpdate implements PacketBuilder {

    /**
     * Prevent instance creation.
     */
    private PlayerUpdate() {
    }

    /**
     * Update the specified player.
     *
     * @param p
     */
    public static void update(Player p) {

    	
        StaticPacketBuilder mainPacket = new StaticPacketBuilder().setId(107).setSize(Size.VAR_SHORT).initBitAccess();
        StaticPacketBuilder updateBlock = new StaticPacketBuilder().setBare(true);
        updateThisPlayerMovement(p, mainPacket);
        if (p.getUpdateFlags().isUpdateRequired()) {
            appendUpdateBlock(p, updateBlock, false);
        }
        mainPacket.addBits(8, p.getPlayerListSize());
        int size = p.getPlayerListSize();
        p.setPlayerListSize(0);
        boolean[] newPlayer = new boolean[ClientConstants.PLAYER_CAP];
        for (int i = 0; i < size; i++) {
            if (p.getPlayerList()[i] == null || p.getPlayerList()[i].isDisconnected() || !p.getPlayerList()[i].getLocation().withinDistance(p.getLocation()) || p.getPlayerList()[i].getUpdateFlags().didTeleport() || p.getPlayerList()[i].isHidden()) {
                if (p.getPlayerList()[i] != null) {
                    p.getPlayersInList()[p.getPlayerList()[i].getIndex()] = 0;
                }
                mainPacket.addBits(1, 1);
                mainPacket.addBits(2, 3);
            } else {
                updatePlayerMovement(p.getPlayerList()[i], mainPacket);
                p.getPlayerList()[p.getPlayerListSize()] = p.getPlayerList()[i];
                p.setPlayerListSize(p.getPlayerListSize() + 1);
            }
        }
        for (Player p2 : World.getInstance().getPlayerList()) {
            if (p2 == null || p2.getIndex() == p.getIndex()) {
                continue;
            }
            if (p.getPlayersInList()[p2.getIndex()] == 1 || !p2.getLocation().withinDistance(p.getLocation()) || p2.isHidden()) {
                continue;
            }
            newPlayer[p.getPlayerListSize()] = true;
            addNewPlayer(p, p2, mainPacket);
        }
        for (int i = 0; i < p.getPlayerListSize(); i++) {
            if (newPlayer[i]) {
                appendUpdateBlock(p.getPlayerList()[i], updateBlock, true);
            } else {
                if (p.getPlayerList()[i].getUpdateFlags().isUpdateRequired()) {
                    appendUpdateBlock(p.getPlayerList()[i], updateBlock, false);
                }
            }
        }
        if (updateBlock.getLength() > 0) {
            mainPacket.addBits(11, 2047);
        }
        mainPacket.finishBitAccess();
        if (updateBlock.getLength() > 0) {
            mainPacket.addBytes(updateBlock.toPacket().getData());
        }
        p.getSession().writePacket(mainPacket.toPacket());
    }

	private static void addNewPlayer(Player p, Player p2, StaticPacketBuilder updateBlock) {
		p.getPlayersInList()[p2.getIndex()] = 1;
		p.getPlayerList()[p.getPlayerListSize()] = p2;
		p.setPlayerListSize(p.getPlayerListSize()+1);
		updateBlock.addBits(11, p2.getIndex());
		int yPos = p2.getLocation().getY() - p.getLocation().getY();
		if(yPos < 0)
			yPos += 32;
		int xPos = p2.getLocation().getX() - p.getLocation().getX();
		if(xPos < 0)
			xPos += 32;
		updateBlock.addBits(5, yPos);
		updateBlock.addBits(5, xPos);
		updateBlock.addBits(1, 1);
		updateBlock.addBits(1, 1);
		updateBlock.addBits(3, 1);
	}

    private static void updatePlayerMovement(Player player, StaticPacketBuilder mainPacket) {
        if (player.getSprites().getPrimarySprite() == -1) {
            if (player.getUpdateFlags().isUpdateRequired()) {
                mainPacket.addBits(1, 1);
                mainPacket.addBits(2, 0);
            } else {
                mainPacket.addBits(1, 0);
            }
        } else if (player.getSprites().getSecondarySprite() == -1) {
            mainPacket.addBits(1, 1);
            mainPacket.addBits(2, 1);
            mainPacket.addBits(3, player.getSprites().getPrimarySprite());
            mainPacket.addBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
        } else {
            mainPacket.addBits(1, 1);
            mainPacket.addBits(2, 2);
			mainPacket.addBits(1, 1); //added this
            mainPacket.addBits(3, player.getSprites().getPrimarySprite());
            mainPacket.addBits(3, player.getSprites().getSecondarySprite());
            mainPacket.addBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
        }
    }

   private static void updateThisPlayerMovement(Player p, StaticPacketBuilder mainPacket) {
        if (p.getUpdateFlags().didTeleport()) {
            mainPacket.addBits(1, 1);
            mainPacket.addBits(2, 3);
            mainPacket.addBits(1, p.getUpdateFlags().isUpdateRequired() ? 1 : 0);
            mainPacket.addBits(2, p.getLocation().getZ());
            mainPacket.addBits(1, 1);
            mainPacket.addBits(7, p.getLocation().getLocalX(p.getUpdateFlags().getLastRegion()));
            mainPacket.addBits(7, p.getLocation().getLocalY(p.getUpdateFlags().getLastRegion()));
        } else {
            if (p.getSprites().getPrimarySprite() == -1) {
                mainPacket.addBits(1, p.getUpdateFlags().isUpdateRequired() ? 1 : 0);
                if (p.getUpdateFlags().isUpdateRequired()) {
                    mainPacket.addBits(2, 0);
                }
            } else {
                if (p.getSprites().getSecondarySprite() != -1) {
                    mainPacket.addBits(1, 1);
                    mainPacket.addBits(2, 2);
					mainPacket.addBits(1, 1);
					mainPacket.addBits(3, p.getSprites().getPrimarySprite());
					mainPacket.addBits(3, p.getSprites().getSecondarySprite());
                    mainPacket.addBits(1, p.getUpdateFlags().isUpdateRequired() ? 1 : 0);
                } else {
                    mainPacket.addBits(1, 1);
                    mainPacket.addBits(2, 1);
                    mainPacket.addBits(3, p.getSprites().getPrimarySprite());
                    mainPacket.addBits(1, p.getUpdateFlags().isUpdateRequired() ? 1 : 0);
                }
            }
        }
    }

    private static void appendUpdateBlock(Player p, StaticPacketBuilder updateBlock, boolean forceAppearance) {
        int mask = 0x0;

        PlayerUpdateFlags flags = p.getUpdateFlags();
	if (flags.isForceTextUpdateRequired()) {
		mask |= 0x20;
	}
        if (flags.isHitUpdateRequired()) {
            mask |= 0x4;
        }
        if (flags.isFaceToUpdateRequired()) {
            mask |= 0x80;
        }
        if (flags.isAppearanceUpdateRequired() || forceAppearance) {
            mask |= 0x10;
        }
        if (flags.isAnimationUpdateRequired()) {
            mask |= 0x8;
        }
        if (flags.isGraphicsUpdateRequired()) {
            mask |= 0x100;
        }
	if (flags.isHit2UpdateRequired()) {
            mask |= 0x400;
        }
        if (flags.isChatTextUpdateRequired()) {
            mask |= 0x40;
        }
        if (mask >= 0x100) {
            mask |= 0x1;
            updateBlock.addByte((byte) (mask & 0xFF));
            updateBlock.addByte((byte) (mask >> 8));
        } else {
            updateBlock.addByte((byte) (mask & 0xFF));
        }
	if (flags.isForceTextUpdateRequired()) {
		appendForceTextUpdate(p, updateBlock);
	}
        if (flags.isHitUpdateRequired()) {
            appendHitUpdate(p, updateBlock);
        }
        if (flags.isFaceToUpdateRequired()) {
            appendFaceToUpdate(p, updateBlock);
        }
        if (flags.isAppearanceUpdateRequired() || forceAppearance) {
            appendAppearanceUpdate(p, updateBlock);
        }
        if (flags.isAnimationUpdateRequired()) {
            appendAnimationUpdate(p, updateBlock);
        }
        if (flags.isGraphicsUpdateRequired()) {
            appendGraphicsUpdate(p, updateBlock);
        }
	if (flags.isHit2UpdateRequired()) {
            appendHit2Update(p, updateBlock);
        }
        if (flags.isChatTextUpdateRequired()) {
            appendChatTextUpdate(p, updateBlock);
        }
    }


    private static void appendForceTextUpdate(Player p, StaticPacketBuilder updateBlock) {
	updateBlock.addString(p.getForceText().getText());
    }

    private static void appendFaceToUpdate(Player p, StaticPacketBuilder updateBlock) {
        updateBlock.addShort(p.getUpdateFlags().getFaceTo());
    }

    private static void appendHit2Update(Player p, StaticPacketBuilder updateBlock) {
        updateBlock.addSmart(p.getHits().getHitDamage2());
        updateBlock.addByte((byte) p.getHits().getHitType2());
    }

    private static void appendHitUpdate(Player p, StaticPacketBuilder updateBlock) {
        updateBlock.addSmart(p.getHits().getHitDamage1());
        updateBlock.addByteA(p.getHits().getHitType1());
        int hpRatio = p.getSkills().getLevel(3) * 255 / p.getSkills().getLevelForXp(3);
        updateBlock.addByteC(hpRatio);
    }

    private static void appendGraphicsUpdate(Player p, StaticPacketBuilder updateBlock) {
        updateBlock.addShort(p.getLastGraphics().getId());
        updateBlock.addInt1(p.getLastGraphics().getDelay());
    }

    private static void appendAnimationUpdate(Player p, StaticPacketBuilder updateBlock) {
        updateBlock.addShortA(p.getLastAnimation().getId());
        updateBlock.addByteS(p.getLastAnimation().getDelay());
    }

    private static void appendChatTextUpdate(Player p, StaticPacketBuilder updateBlock) {
        updateBlock.addLEShortA(0);
        updateBlock.addByteA(p.getRights());
        byte[] chatStr = new byte[256];
        chatStr[0] = (byte) p.getLastChatMessage().getChatText().length();
        int offset = 1 + Misc.encryptPlayerChat(chatStr, 0, 1, p.getLastChatMessage().getChatText().length(), p.getLastChatMessage().getChatText().getBytes());
        updateBlock.addByteS(offset);
	updateBlock.addBytes(chatStr, 0, offset);   
    }

    private static void appendAppearanceUpdate(Player p, StaticPacketBuilder updateBlock) {
        StaticPacketBuilder playerProps = new StaticPacketBuilder().setBare(true);
        Appearance app = p.getAppearance();
        playerProps.addByte((byte) (app.getGender() & 0xFF));
        playerProps.addByte((byte) 0);
        playerProps.addByte((byte) p.getHeadIcons().getPkIcon());
        playerProps.addByte((byte) p.getHeadIcons().getPrayerIcon(p));
        if (!app.isNpc()) {
            for (int i = 0; i < 4; i++) {
                if (p.getEquipment().get(i) != null) {
                    playerProps.addShort(32768 + p.getEquipment().get(i).getDefinition().getEquipId());
                } else {
                    playerProps.addByte((byte) 0);
                }
            }
            if (p.getEquipment().get(Equipment.SLOT_CHEST) != null) {
                playerProps.addShort(32768 + p.getEquipment().get(Equipment.SLOT_CHEST).getDefinition().getEquipId());
            } else {
                playerProps.addShort(0x100 + app.getLook(2));
            }
            if (p.getEquipment().get(Equipment.SLOT_SHIELD) != null) {
                playerProps.addShort(32768 + p.getEquipment().get(Equipment.SLOT_SHIELD).getDefinition().getEquipId());
            } else {
                playerProps.addByte((byte) 0);
            }
            Item chest = p.getEquipment().get(Equipment.SLOT_CHEST);
            if (chest != null) {
                if (!Equipment.isFullBody(chest.getDefinition())) {
                    playerProps.addShort(0x100 + app.getLook(3));
                } else {
                    playerProps.addByte((byte) 0);
                }
            } else {
                playerProps.addShort(0x100 + app.getLook(3));
            }
            if (p.getEquipment().get(Equipment.SLOT_LEGS) != null) {
                playerProps.addShort(32768 + p.getEquipment().get(Equipment.SLOT_LEGS).getDefinition().getEquipId());
            } else {
                playerProps.addShort(0x100 + app.getLook(5));
            }
            Item hat = p.getEquipment().get(Equipment.SLOT_HAT);
            if (hat != null) {
                if (!Equipment.isFullHat(hat.getDefinition()) && !Equipment.isFullMask(hat.getDefinition())) {
                    playerProps.addShort(0x100 + app.getLook(0));
                } else {
                    playerProps.addByte((byte) 0);
                }
            } else {
                playerProps.addShort(0x100 + app.getLook(0));
            }
            if (p.getEquipment().get(Equipment.SLOT_HANDS) != null) {
                playerProps.addShort(32768 + p.getEquipment().get(Equipment.SLOT_HANDS).getDefinition().getEquipId());
            } else {
                playerProps.addShort(0x100 + app.getLook(4));
            }
            if (p.getEquipment().get(Equipment.SLOT_FEET) != null) {
                playerProps.addShort(32768 + p.getEquipment().get(Equipment.SLOT_FEET).getDefinition().getEquipId());
            } else {
                playerProps.addShort(0x100 + app.getLook(6));
            }
            if (hat != null) {
                if (!Equipment.isFullMask(hat.getDefinition())) {
                    playerProps.addShort(0x100 + app.getLook(1));
                } else {
                    playerProps.addByte((byte) 0);
                }
            } else {
                playerProps.addShort(0x100 + app.getLook(1));
            }
        } else {
            playerProps.addShort(-1);
            playerProps.addShort(app.getNpcId());
            playerProps.addByte((byte) 0);

        }

        for (int colour : app.getColours()) {
            playerProps.addByte((byte) colour);
        }

        playerProps.addShort(p.getEquipment().getWepEmotes());
        playerProps.addString(Misc.upper2(p.getUsername().replaceAll("_", " ").trim()));
        playerProps.addByte((byte) p.getSkills().getCombatLevel());
        playerProps.addShort(0);
        playerProps.addShort(0);
        updateBlock.addByte((byte) (playerProps.getLength() & 0xFF));
        for (int i = 0; i < playerProps.getLength(); i++)
            updateBlock.addByte((byte) (-128 + playerProps.getPayload()[i]));
    }

}

