package online.kingdomkeys.kingdomkeys.integration.shouldersurfing;

import com.github.exopandora.shouldersurfing.api.client.ShoulderSurfing;
import com.github.exopandora.shouldersurfing.client.ShoulderSurfingCamera;
import com.github.exopandora.shouldersurfing.client.ShoulderSurfingImpl;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

public class KKShoulderSurfing {

    public static void enableDecoupling() {
        if (ModConfigs.shoulderSurfingDecoupled && !ShoulderSurfingImpl.getInstance().isCameraDecoupled()) {
            ShoulderSurfingImpl.getInstance().toggleCameraCoupling();
        }
    }

    public static void disableDecoupling() {
        if (ShoulderSurfingImpl.getInstance().isCameraDecoupled()) {
            ShoulderSurfingImpl.getInstance().toggleCameraCoupling();
        }
    }

    public static void setCameraPos(float currentYaw, float currentPitch, float yawCorrection, float pitchCorrection, float CORRECTION_SMOOTH) {
        ShoulderSurfingCamera camera = ShoulderSurfingImpl.getInstance().getCamera();
        camera.setYRot(currentYaw + yawCorrection * CORRECTION_SMOOTH);
        camera.setXRot(currentPitch + pitchCorrection * CORRECTION_SMOOTH);
    }

}
