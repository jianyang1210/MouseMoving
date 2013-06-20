package com.yj.mm;

import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinUser.*;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.yj.frame.Client;;

public class KeyHook {
    private static boolean quit;
    private static HHOOK hhk;
    private static LowLevelKeyboardProc keyboardHook;
    private static int preCode = 0;
    
    public static void main(String[] args) {
    	final Client client = new Client();
        final User32 lib = User32.INSTANCE;
        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        keyboardHook = new LowLevelKeyboardProc() {
        
			@Override
			public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT info) {
				 if (nCode >= 0) {
					 System.out.println(info.vkCode + "once");
					 int vkCode = info.vkCode;
					 if (vkCode == 81 && preCode != vkCode ) {
                         quit = true;
                     }else if(vkCode == 121 && preCode != vkCode ){
                    	 client.flag = true;
                     }else if(vkCode == 120 && preCode != vkCode ){
                    	 client.flag = false;
                     }else if(vkCode == 122 && preCode != vkCode ){
//                    	 client.requestFocus();
                    	 client.listAdd();
                     }
					 preCode = vkCode;
				 }
				return lib.CallNextHookEx(hhk, nCode, wParam, info.getPointer());
			}
        };
        hhk = lib.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0);
        System.out.println("Keyboard hook installed, type anywhere, 'q' to quit");
        new Thread() {
            public void run() {
                while (!quit) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.err.println("unhook and exit");
                lib.UnhookWindowsHookEx(hhk);
                System.exit(0);
            }
        }.start();

        // 以下部分是干嘛的？
//        实际上while循环一次都不执行，这些代码的作用我理解是让程序在GetMessage函数这里阻塞，不然程序就结束了。
        int result;
        MSG msg = new MSG();
        while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                System.err.println("error in get message");
                break;
            } else {
                System.err.println("got message");
                lib.TranslateMessage(msg);
                System.out.println("the message is " + msg + " ");
                lib.DispatchMessage(msg);
            }
        }
        lib.UnhookWindowsHookEx(hhk);
    }
}
