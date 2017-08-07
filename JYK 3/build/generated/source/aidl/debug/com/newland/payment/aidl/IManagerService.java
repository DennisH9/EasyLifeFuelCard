/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace-as\\EasyLifeFuelCard\\JYK 3\\src\\com\\newland\\payment\\aidl\\IManagerService.aidl
 */
package com.newland.payment.aidl;
public interface IManagerService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.newland.payment.aidl.IManagerService
{
private static final java.lang.String DESCRIPTOR = "com.newland.payment.aidl.IManagerService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.newland.payment.aidl.IManagerService interface,
 * generating a proxy if needed.
 */
public static com.newland.payment.aidl.IManagerService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.newland.payment.aidl.IManagerService))) {
return ((com.newland.payment.aidl.IManagerService)iin);
}
return new com.newland.payment.aidl.IManagerService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_reset:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.reset();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_activate:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.activate();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.newland.payment.aidl.IManagerService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
     * 初始化
     * true 成功，false 失败
     */
@Override public boolean reset() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_reset, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * 激活
     * true 成功，false 失败
     */
@Override public boolean activate() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_activate, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_reset = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_activate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
/**
     * 初始化
     * true 成功，false 失败
     */
public boolean reset() throws android.os.RemoteException;
/**
     * 激活
     * true 成功，false 失败
     */
public boolean activate() throws android.os.RemoteException;
}
