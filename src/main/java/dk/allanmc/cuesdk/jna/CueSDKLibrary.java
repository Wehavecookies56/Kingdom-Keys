package dk.allanmc.cuesdk.jna;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

public abstract interface CueSDKLibrary extends Library {
	public static final String JNA_LIBRARY_NAME = "CueSDK";
	public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance("CueSDK");
	public static final CueSDKLibrary INSTANCE = (CueSDKLibrary) Native.loadLibrary("CueSDK", CueSDKLibrary.class);

	public abstract byte CorsairSetLedsColors(int paramInt, CorsairLedColor paramCorsairLedColor);

	public abstract byte CorsairSetLedsColorsAsync(int paramInt, CorsairLedColor paramCorsairLedColor, CorsairSetLedsColorsAsync_CallbackType_callback paramCorsairSetLedsColorsAsync_CallbackType_callback, Pointer paramPointer);

	public abstract int CorsairGetDeviceCount();

	public abstract CorsairDeviceInfo CorsairGetDeviceInfo(int paramInt);

	public abstract CorsairLedPositions CorsairGetLedPositions();

	public abstract int CorsairGetLedIdForKeyName(byte paramByte);

	public abstract byte CorsairRequestControl(int paramInt);

	public abstract CorsairProtocolDetails.ByValue CorsairPerformProtocolHandshake();

	public abstract int CorsairGetLastError();

	public abstract byte CorsairReleaseControl(int paramInt);

	public static abstract interface CorsairSetLedsColorsAsync_CallbackType_callback extends Callback {
		public abstract void apply(Pointer paramPointer, byte paramByte, int paramInt);
	}

	public static abstract interface CorsairError {
		public static final int CE_Success = 0;
		public static final int CE_ServerNotFound = 1;
		public static final int CE_NoControl = 2;
		public static final int CE_ProtocolHandshakeMissing = 3;
		public static final int CE_IncompatibleProtocol = 4;
		public static final int CE_InvalidArguments = 5;
	}

	public static abstract interface CorsairAccessMode {
		public static final int CAM_ExclusiveLightingControl = 0;
	}

	public static abstract interface CorsairDeviceCaps {
		public static final int CDC_None = 0;
		public static final int CDC_Lighting = 1;
	}

	public static abstract interface CorsairLogicalLayout {
		public static final int CLL_Invalid = 0;
		public static final int CLL_US_Int = 1;
		public static final int CLL_NA = 2;
		public static final int CLL_EU = 3;
		public static final int CLL_UK = 4;
		public static final int CLL_BE = 5;
		public static final int CLL_BR = 6;
		public static final int CLL_CH = 7;
		public static final int CLL_CN = 8;
		public static final int CLL_DE = 9;
		public static final int CLL_ES = 10;
		public static final int CLL_FR = 11;
		public static final int CLL_IT = 12;
		public static final int CLL_ND = 13;
		public static final int CLL_RU = 14;
		public static final int CLL_JP = 15;
		public static final int CLL_KR = 16;
		public static final int CLL_TW = 17;
		public static final int CLL_MEX = 18;
	}

	public static abstract interface CorsairPhysicalLayout {
		public static final int CPL_Invalid = 0;
		public static final int CPL_US = 1;
		public static final int CPL_UK = 2;
		public static final int CPL_BR = 3;
		public static final int CPL_JP = 4;
		public static final int CPL_KR = 5;
		public static final int CPL_Zones1 = 6;
		public static final int CPL_Zones2 = 7;
		public static final int CPL_Zones3 = 8;
		public static final int CPL_Zones4 = 9;
	}

	public static abstract interface CorsairDeviceType {
		public static final int CDT_Unknown = 0;
		public static final int CDT_Mouse = 1;
		public static final int CDT_Keyboard = 2;
		public static final int CDT_Headset = 3;
	}

	public static abstract interface CorsairLedId {
		public static final int CLI_Invalid = 0;
		public static final int CLK_Escape = 1;
		public static final int CLK_F1 = 2;
		public static final int CLK_F2 = 3;
		public static final int CLK_F3 = 4;
		public static final int CLK_F4 = 5;
		public static final int CLK_F5 = 6;
		public static final int CLK_F6 = 7;
		public static final int CLK_F7 = 8;
		public static final int CLK_F8 = 9;
		public static final int CLK_F9 = 10;
		public static final int CLK_F10 = 11;
		public static final int CLK_F11 = 12;
		public static final int CLK_GraveAccentAndTilde = 13;
		public static final int CLK_1 = 14;
		public static final int CLK_2 = 15;
		public static final int CLK_3 = 16;
		public static final int CLK_4 = 17;
		public static final int CLK_5 = 18;
		public static final int CLK_6 = 19;
		public static final int CLK_7 = 20;
		public static final int CLK_8 = 21;
		public static final int CLK_9 = 22;
		public static final int CLK_0 = 23;
		public static final int CLK_MinusAndUnderscore = 24;
		public static final int CLK_Tab = 25;
		public static final int CLK_Q = 26;
		public static final int CLK_W = 27;
		public static final int CLK_E = 28;
		public static final int CLK_R = 29;
		public static final int CLK_T = 30;
		public static final int CLK_Y = 31;
		public static final int CLK_U = 32;
		public static final int CLK_I = 33;
		public static final int CLK_O = 34;
		public static final int CLK_P = 35;
		public static final int CLK_BracketLeft = 36;
		public static final int CLK_CapsLock = 37;
		public static final int CLK_A = 38;
		public static final int CLK_S = 39;
		public static final int CLK_D = 40;
		public static final int CLK_F = 41;
		public static final int CLK_G = 42;
		public static final int CLK_H = 43;
		public static final int CLK_J = 44;
		public static final int CLK_K = 45;
		public static final int CLK_L = 46;
		public static final int CLK_SemicolonAndColon = 47;
		public static final int CLK_ApostropheAndDoubleQuote = 48;
		public static final int CLK_LeftShift = 49;
		public static final int CLK_NonUsBackslash = 50;
		public static final int CLK_Z = 51;
		public static final int CLK_X = 52;
		public static final int CLK_C = 53;
		public static final int CLK_V = 54;
		public static final int CLK_B = 55;
		public static final int CLK_N = 56;
		public static final int CLK_M = 57;
		public static final int CLK_CommaAndLessThan = 58;
		public static final int CLK_PeriodAndBiggerThan = 59;
		public static final int CLK_SlashAndQuestionMark = 60;
		public static final int CLK_LeftCtrl = 61;
		public static final int CLK_LeftGui = 62;
		public static final int CLK_LeftAlt = 63;
		public static final int CLK_Lang2 = 64;
		public static final int CLK_Space = 65;
		public static final int CLK_Lang1 = 66;
		public static final int CLK_International2 = 67;
		public static final int CLK_RightAlt = 68;
		public static final int CLK_RightGui = 69;
		public static final int CLK_Application = 70;
		public static final int CLK_LedProgramming = 71;
		public static final int CLK_Brightness = 72;
		public static final int CLK_F12 = 73;
		public static final int CLK_PrintScreen = 74;
		public static final int CLK_ScrollLock = 75;
		public static final int CLK_PauseBreak = 76;
		public static final int CLK_Insert = 77;
		public static final int CLK_Home = 78;
		public static final int CLK_PageUp = 79;
		public static final int CLK_BracketRight = 80;
		public static final int CLK_Backslash = 81;
		public static final int CLK_NonUsTilde = 82;
		public static final int CLK_Enter = 83;
		public static final int CLK_International1 = 84;
		public static final int CLK_EqualsAndPlus = 85;
		public static final int CLK_International3 = 86;
		public static final int CLK_Backspace = 87;
		public static final int CLK_Delete = 88;
		public static final int CLK_End = 89;
		public static final int CLK_PageDown = 90;
		public static final int CLK_RightShift = 91;
		public static final int CLK_RightCtrl = 92;
		public static final int CLK_UpArrow = 93;
		public static final int CLK_LeftArrow = 94;
		public static final int CLK_DownArrow = 95;
		public static final int CLK_RightArrow = 96;
		public static final int CLK_WinLock = 97;
		public static final int CLK_Mute = 98;
		public static final int CLK_Stop = 99;
		public static final int CLK_ScanPreviousTrack = 100;
		public static final int CLK_PlayPause = 101;
		public static final int CLK_ScanNextTrack = 102;
		public static final int CLK_NumLock = 103;
		public static final int CLK_KeypadSlash = 104;
		public static final int CLK_KeypadAsterisk = 105;
		public static final int CLK_KeypadMinus = 106;
		public static final int CLK_KeypadPlus = 107;
		public static final int CLK_KeypadEnter = 108;
		public static final int CLK_Keypad7 = 109;
		public static final int CLK_Keypad8 = 110;
		public static final int CLK_Keypad9 = 111;
		public static final int CLK_KeypadComma = 112;
		public static final int CLK_Keypad4 = 113;
		public static final int CLK_Keypad5 = 114;
		public static final int CLK_Keypad6 = 115;
		public static final int CLK_Keypad1 = 116;
		public static final int CLK_Keypad2 = 117;
		public static final int CLK_Keypad3 = 118;
		public static final int CLK_Keypad0 = 119;
		public static final int CLK_KeypadPeriodAndDelete = 120;
		public static final int CLK_G1 = 121;
		public static final int CLK_G2 = 122;
		public static final int CLK_G3 = 123;
		public static final int CLK_G4 = 124;
		public static final int CLK_G5 = 125;
		public static final int CLK_G6 = 126;
		public static final int CLK_G7 = 127;
		public static final int CLK_G8 = 128;
		public static final int CLK_G9 = 129;
		public static final int CLK_G10 = 130;
		public static final int CLK_VolumeUp = 131;
		public static final int CLK_VolumeDown = 132;
		public static final int CLK_MR = 133;
		public static final int CLK_M1 = 134;
		public static final int CLK_M2 = 135;
		public static final int CLK_M3 = 136;
		public static final int CLK_G11 = 137;
		public static final int CLK_G12 = 138;
		public static final int CLK_G13 = 139;
		public static final int CLK_G14 = 140;
		public static final int CLK_G15 = 141;
		public static final int CLK_G16 = 142;
		public static final int CLK_G17 = 143;
		public static final int CLK_G18 = 144;
		public static final int CLK_International5 = 145;
		public static final int CLK_International4 = 146;
		public static final int CLK_Fn = 147;
		public static final int CLM_1 = 148;
		public static final int CLM_2 = 149;
		public static final int CLM_3 = 150;
		public static final int CLM_4 = 151;
		public static final int CLH_LeftLogo = 152;
		public static final int CLH_RightLogo = 153;
		public static final int CLK_Logo = 154;
		public static final int CLI_Last = 154;
	}
}
