LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := TranTracker
LOCAL_SRC_FILES := TranTracker.cpp

include $(BUILD_SHARED_LIBRARY)
