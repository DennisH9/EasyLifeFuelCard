/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace-as\\EasyLifeFuelCard\\JYK 3\\src\\com\\newland\\payment\\aidl\\IPaymentService.aidl
 */
package com.newland.payment.aidl;
public interface IPaymentService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.newland.payment.aidl.IPaymentService
{
private static final java.lang.String DESCRIPTOR = "com.newland.payment.aidl.IPaymentService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.newland.payment.aidl.IPaymentService interface,
 * generating a proxy if needed.
 */
public static com.newland.payment.aidl.IPaymentService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.newland.payment.aidl.IPaymentService))) {
return ((com.newland.payment.aidl.IPaymentService)iin);
}
return new com.newland.payment.aidl.IPaymentService.Stub.Proxy(obj);
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
case TRANSACTION_setParam:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
boolean _result = this.setParam(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getParam:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getParam(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_rePrint:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _result = this.rePrint(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_findWater:
{
data.enforceInterface(DESCRIPTOR);
com.newland.payment.aidl.SimpleWater _arg0;
if ((0!=data.readInt())) {
_arg0 = com.newland.payment.aidl.SimpleWater.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
int _result = this.findWater(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
if ((_arg0!=null)) {
reply.writeInt(1);
_arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_rePrintSettle:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.rePrintSettle();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_findWaterByOutOrderNo:
{
data.enforceInterface(DESCRIPTOR);
com.newland.payment.aidl.SimpleWater _arg0;
if ((0!=data.readInt())) {
_arg0 = com.newland.payment.aidl.SimpleWater.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.lang.String _arg1;
_arg1 = data.readString();
int _result = this.findWaterByOutOrderNo(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
if ((_arg0!=null)) {
reply.writeInt(1);
_arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.newland.payment.aidl.IPaymentService
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
     * 设置参数
     * @param key 参数key
     * @param value 参数值
     */
@Override public boolean setParam(java.lang.String key, java.lang.String value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
_data.writeString(value);
mRemote.transact(Stub.TRANSACTION_setParam, _data, _reply, 0);
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
     * 获取参数
     * @param key 参数key
     * @return 返回打印张数
     */
@Override public java.lang.String getParam(java.lang.String key) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
mRemote.transact(Stub.TRANSACTION_getParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int rePrint(java.lang.String traceNo, java.lang.String transTime) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(traceNo);
_data.writeString(transTime);
mRemote.transact(Stub.TRANSACTION_rePrint, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int findWater(com.newland.payment.aidl.SimpleWater water, java.lang.String voucherNo, java.lang.String transTime) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((water!=null)) {
_data.writeInt(1);
water.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(voucherNo);
_data.writeString(transTime);
mRemote.transact(Stub.TRANSACTION_findWater, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
if ((0!=_reply.readInt())) {
water.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int rePrintSettle() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_rePrintSettle, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int findWaterByOutOrderNo(com.newland.payment.aidl.SimpleWater water, java.lang.String outOrderNo) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((water!=null)) {
_data.writeInt(1);
water.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(outOrderNo);
mRemote.transact(Stub.TRANSACTION_findWaterByOutOrderNo, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
if ((0!=_reply.readInt())) {
water.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_setParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_rePrint = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_findWater = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_rePrintSettle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_findWaterByOutOrderNo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
/**
     * 设置参数
     * @param key 参数key
     * @param value 参数值
     */
public boolean setParam(java.lang.String key, java.lang.String value) throws android.os.RemoteException;
/**
     * 获取参数
     * @param key 参数key
     * @return 返回打印张数
     */
public java.lang.String getParam(java.lang.String key) throws android.os.RemoteException;
public int rePrint(java.lang.String traceNo, java.lang.String transTime) throws android.os.RemoteException;
public int findWater(com.newland.payment.aidl.SimpleWater water, java.lang.String voucherNo, java.lang.String transTime) throws android.os.RemoteException;
public int rePrintSettle() throws android.os.RemoteException;
public int findWaterByOutOrderNo(com.newland.payment.aidl.SimpleWater water, java.lang.String outOrderNo) throws android.os.RemoteException;
}
