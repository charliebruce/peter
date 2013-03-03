package ember.server.game;

/**
 * Manages update flags.
 *
 * @author Graham
 */
public class PlayerUpdateFlags {


    private boolean forceTextUpdateRequired = false, appearanceUpdateRequired = true, chatTextUpdateRequired = false, animationUpdateRequired = false, graphicsUpdateRequired = false, hitUpdateRequired = false, hit2UpdateRequired = false;


    public boolean isUpdateRequired() {
        return appearanceUpdateRequired || chatTextUpdateRequired || animationUpdateRequired || graphicsUpdateRequired || hitUpdateRequired || hit2UpdateRequired;
    }



    public void clear() {
        appearanceUpdateRequired = false;
        chatTextUpdateRequired = false;
        animationUpdateRequired = false;
        graphicsUpdateRequired = false;
        hitUpdateRequired = false;
        hit2UpdateRequired = false;
    }

    public boolean isAppearanceUpdateRequired() {
        return appearanceUpdateRequired;
    }

    public boolean isGraphicsUpdateRequired() {
        return graphicsUpdateRequired;
    }

    public void setGraphicsUpdateRequired(boolean b) {
        this.graphicsUpdateRequired = b;
    }


    public void setAppearanceUpdateRequired(boolean b) {
        appearanceUpdateRequired = b;
    }

    public void setChatTextUpdateRequired(boolean b) {
        chatTextUpdateRequired = b;
    }

    public boolean isChatTextUpdateRequired() {
        return chatTextUpdateRequired;
    }

    public void setAnimationUpdateRequired(boolean b) {
        this.animationUpdateRequired = b;
    }

    public boolean isAnimationUpdateRequired() {
        return this.animationUpdateRequired;
    }

    public void setHitUpdateRequired(boolean b) {
        this.hitUpdateRequired = b;
    }

    public boolean isHitUpdateRequired() {
        return this.hitUpdateRequired;
    }

    public void setHit2UpdateRequired(boolean b) {
        this.hit2UpdateRequired = b;
    }

    public boolean isHit2UpdateRequired() {
        return this.hit2UpdateRequired;
    }

    public void setForceTextUpdateRequired(boolean b) {
	appearanceUpdateRequired = b;
	this.forceTextUpdateRequired = b;
    }

    public boolean isForceTextUpdateRequired() {
	return forceTextUpdateRequired;
    }

}
