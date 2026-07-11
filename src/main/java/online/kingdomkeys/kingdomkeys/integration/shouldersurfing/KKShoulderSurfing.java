package online.kingdomkeys.kingdomkeys.integration.shouldersurfing;

import com.github.exopandora.shouldersurfing.client.ShoulderSurfing;
import com.github.exopandora.shouldersurfing.client.ShoulderSurfingCamera;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

public class KKShoulderSurfing {

    public static void enableDecoupling() {
        if (ModConfigs.shoulderSurfingDecoupled && !ShoulderSurfing.getInstance().isCameraDecoupled()) {
            ShoulderSurfing.getInstance().toggleCameraCoupling();
        }
    }

    public static void disableDecoupling() {
        if (ShoulderSurfing.getInstance().isCameraDecoupled()) {
            ShoulderSurfing.getInstance().toggleCameraCoupling();
        }
    }

    public static void setCameraPos(float currentYaw, float currentPitch, float yawCorrection, float pitchCorrection, float CORRECTION_SMOOTH) {
        ShoulderSurfingCamera camera = ShoulderSurfing.getInstance().getCamera();
        camera.setYRot(currentYaw + yawCorrection * CORRECTION_SMOOTH);
        camera.setXRot(currentPitch + pitchCorrection * CORRECTION_SMOOTH);
    }

}
