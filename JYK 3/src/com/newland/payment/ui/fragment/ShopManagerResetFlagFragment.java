package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.newland.base.CommonThread;
import com.newland.base.util.MessageUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.base.util.ToastUtils;
import com.newland.emv.EmvAppModule;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.interfaces.ThreadCallBack;
import com.newland.payment.mvc.service.CommonDBService;
import com.newland.payment.mvc.service.CommonDBService.CleanType;
import com.newland.payment.mvc.service.impl.CommonDBServiceImpl;
import com.newland.payment.ui.view.CommonDialog;
import com.newland.payment.ui.view.SettingSwitchView;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.emv.EmvModule;
import com.newland.pos.sdk.util.FormatUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 清标志
 *
 * @author linst
 * @date 2015-5-22
 * @time 
 */
public class ShopManagerResetFlagFragment extends BaseFragment{
	private CommonDialog dialog;
	
	/** 冲正测试开关 */
//	@ViewInject(R.id.set_is_reverse)
	SettingSwitchView setIsReverse;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.shop_manager_reset_flag_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}
	
	
	@Override
	protected void initData() {
		setTitle(R.string.common_reset_flag);
		
		setIsReverse.setParamData(R.string.is_reverse_test, ParamsConst.PARAMS_IS_REVERSE_TEST);

	}
//	@OnClick({R.id.txt_clear_reverse_flag,R.id.txt_set_no_sign,R.id.txt_set_signed,R.id.txt_clear_script_flag,R.id.txt_clear_emv,R.id.txt_set_emv})
	@Override
	protected void initClickEvent(View view) {
		int i = view.getId();
		if (i == R.id.txt_clear_reverse_flag) {//清冲正流水
			dialog = MessageUtils.showCommonDialog(context, R.string.setting_clear_rush_water,
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							new CommonThread(new ThreadCallBack() {

								@Override
								public void onMain() {
									dialog.dismiss();
									ToastUtils.show(context, R.string.setting_clear_rush_water_succ);
								}

								@Override
								public void onBackGround() {
									CommonDBService service = new CommonDBServiceImpl(context);
									service.cleanWater(CleanType.REVERSE_WATER);
									ParamsUtils.setInt(ParamsTrans.PARAMS_TIMES_REVERSAL_HAVE_SEND, 0);
								}
							}).start();
						}
					});

		} else if (i == R.id.txt_set_no_sign) {//清签到标志
			dialog = MessageUtils.showCommonDialog(context, R.string.common_set_no_sign,
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							new CommonThread(new ThreadCallBack() {

								@Override
								public void onMain() {
									dialog.dismiss();
									ToastUtils.show(context, R.string.common_set_succ);
								}

								@Override
								public void onBackGround() {
									// 设置签到状态
									ParamsUtils.setBoolean(ParamsTrans.PARAMS_FLAG_SIGN, false);
									ParamsUtils.setString(ParamsConst.PARAMS_RUN_LOGIN_DATE, null);

								}
							}).start();

						}
					});

		} else if (i == R.id.txt_set_signed) {//设置已签到标志
			dialog = MessageUtils.showCommonDialog(context, R.string.common_set_signed,
					new OnClickListener() {

						@Override
						public void onClick(View v) {


							new CommonThread(new ThreadCallBack() {

								@Override
								public void onMain() {
									dialog.dismiss();
									ToastUtils.show(context, R.string.common_set_succ);
								}

								@SuppressLint("SimpleDateFormat")
								@Override
								public void onBackGround() {
									// 设置签到状态
									SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
									Date curDate = new Date(System.currentTimeMillis());//获取当前时间
									String strDate = FormatUtils.getDate(formatter.format(curDate));

									ParamsUtils.setBoolean(ParamsTrans.PARAMS_FLAG_SIGN, true);
									ParamsUtils.setString(ParamsConst.PARAMS_RUN_LOGIN_DATE, strDate);

								}
							}).start();
						}
					});

		} else if (i == R.id.txt_clear_script_flag) {//清脚本执行结果流水
			dialog = MessageUtils.showCommonDialog(context, R.string.clear_script_execution_result_of_the_water,
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							new CommonThread(new ThreadCallBack() {

								@Override
								public void onMain() {
									dialog.dismiss();
									ToastUtils.show(context, R.string.common_clear_succ);
								}

								@Override
								public void onBackGround() {
									CommonDBService service = new CommonDBServiceImpl(context);
									service.cleanWater(CleanType.SCRIPT_RESULT);
								}
							}).start();
						}
					});

		} else if (i == R.id.txt_clear_emv) {//清emv
			dialog = MessageUtils.showCommonDialog(context, R.string.common_clear_emv,
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							new CommonThread(new ThreadCallBack() {

								@Override
								public void onMain() {
									dialog.dismiss();
									ToastUtils.show(context, R.string.common_clear_succ);
								}

								@Override
								public void onBackGround() {
									EmvAppModule.getInstance().initEmvParams();
									ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_AID_DOWN, true);
									ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CAPK_DOWN, true);
									ParamsUtils.setInt(ParamsTrans.PARAMS_EMV_TRANS_SERIAL, 1);

								}
							}).start();
						}
					});

		} else if (i == R.id.txt_set_emv) {//设置默认emv参数
			dialog = MessageUtils.showCommonDialog(context, R.string.common_set_emv,
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							new CommonThread(new ThreadCallBack() {

								@Override
								public void onMain() {
									dialog.dismiss();
									ToastUtils.show(context, R.string.common_set_succ);
								}

								@Override
								public void onBackGround() {
									EmvAppModule.getInstance().initEmvParams();
									EmvModule emvModule = EmvAppModule.getInstance();
									emvModule.setCAPK("9F0605A0000000659F220109DF05083230353031323331DF060101DF070101DF028180B72A8FEF5B27F2B550398FDCC256F714BAD497FF56094B7408328CB626AA6F0E6A9DF8388EB9887BC930170BCC1213E90FC070D52C8DCD0FF9E10FAD36801FE93FC998A721705091F18BC7C98241CADC15A2B9DA7FB963142C0AB640D5D0135E77EBAE95AF1B4FEFADCF9C012366BDDA0455C1564A68810D7127676D493890BDDF040103DF03144410C6D51C2F83ADFD92528FA6E38A32DF048D0A");
									emvModule.setCAPK("9F0605A0000000659F220110DF05083230353031323331DF060101DF070101DF02819099B63464EE0B4957E4FD23BF923D12B61469B8FFF8814346B2ED6A780F8988EA9CF0433BC1E655F05EFA66D0C98098F25B659D7A25B8478A36E489760D071F54CDF7416948ED733D816349DA2AADDA227EE45936203CBF628CD033AABA5E5A6E4AE37FBACB4611B4113ED427529C636F6C3304F8ABDD6D9AD660516AE87F7F2DDF1D2FA44C164727E56BBC9BA23C0285DF040103DF0314C75E5210CBE6E8F0594A0F1911B07418CADB5BAB");
									emvModule.setCAPK("9F0605A0000000659F220112DF05083230353031323331DF060101DF070101DF0281B0ADF05CD4C5B490B087C3467B0F3043750438848461288BFEFD6198DD576DC3AD7A7CFA07DBA128C247A8EAB30DC3A30B02FCD7F1C8167965463626FEFF8AB1AA61A4B9AEF09EE12B009842A1ABA01ADB4A2B170668781EC92B60F605FD12B2B2A6F1FE734BE510F60DC5D189E401451B62B4E06851EC20EBFF4522AACC2E9CDC89BC5D8CDE5D633CFD77220FF6BBD4A9B441473CC3C6FEFC8D13E57C3DE97E1269FA19F655215B23563ED1D1860D8681DF040103DF0314874B379B7F607DC1CAF87A19E400B6A9E25163E8");
									emvModule.setCAPK("9F0605A0000000659F220114DF05083230353031323331DF060101DF070101DF0281F8AEED55B9EE00E1ECEB045F61D2DA9A66AB637B43FB5CDBDB22A2FBB25BE061E937E38244EE5132F530144A3F268907D8FD648863F5A96FED7E42089E93457ADC0E1BC89C58A0DB72675FBC47FEE9FF33C16ADE6D341936B06B6A6F5EF6F66A4EDD981DF75DA8399C3053F430ECA342437C23AF423A211AC9F58EAF09B0F837DE9D86C7109DB1646561AA5AF0289AF5514AC64BC2D9D36A179BB8A7971E2BFA03A9E4B847FD3D63524D43A0E8003547B94A8A75E519DF3177D0A60BC0B4BAB1EA59A2CBB4D2D62354E926E9C7D3BE4181E81BA60F8285A896D17DA8C3242481B6C405769A39D547C74ED9FF95A70A796046B5EFF36682DC29DF040103DF0314C0D15F6CD957E491DB56DCDD1CA87A03EBE06B7B");
									emvModule.setCAPK("9F0605A0000003339F220101DF05083230353031323331DF060101DF070101DF028180BBE9066D2517511D239C7BFA77884144AE20C7372F515147E8CE6537C54C0A6A4D45F8CA4D290870CDA59F1344EF71D17D3F35D92F3F06778D0D511EC2A7DC4FFEADF4FB1253CE37A7B2B5A3741227BEF72524DA7A2B7B1CB426BEE27BC513B0CB11AB99BC1BC61DF5AC6CC4D831D0848788CD74F6D543AD37C5A2B4C5D5A93BDF040103DF0314E881E390675D44C2DD81234DCE29C3F5AB2297A0");
									emvModule.setCAPK("9F0605A0000003339F220101DF05083230353031323331DF060101DF070101DF028180BBE9066D2517511D239C7BFA77884144AE20C7372F515147E8CE6537C54C0A6A4D45F8CA4D290870CDA59F1344EF71D17D3F35D92F3F06778D0D511EC2A7DC4FFEADF4FB1253CE37A7B2B5A3741227BEF72524DA7A2B7B1CB426BEE27BC513B0CB11AB99BC1BC61DF5AC6CC4D831D0848788CD74F6D543AD37C5A2B4C5D5A93BDF040103DF0314E881E390675D44C2DD81234DCE29C3F5AB2297A0");
									emvModule.setCAPK("9F0605A0000003339F220101DF05083230353031323331DF060101DF070101DF028180BBE9066D2517511D239C7BFA77884144AE20C7372F515147E8CE6537C54C0A6A4D45F8CA4D290870CDA59F1344EF71D17D3F35D92F3F06778D0D511EC2A7DC4FFEADF4FB1253CE37A7B2B5A3741227BEF72524DA7A2B7B1CB426BEE27BC513B0CB11AB99BC1BC61DF5AC6CC4D831D0848788CD74F6D543AD37C5A2B4C5D5A93BDF040103DF0314E881E390675D44C2DD81234DCE29C3F5AB2297A0");
									emvModule.setCAPK("9F0605A0000003339F220102DF05083230353031323331DF060101DF070101DF028190A3767ABD1B6AA69D7F3FBF28C092DE9ED1E658BA5F0909AF7A1CCD907373B7210FDEB16287BA8E78E1529F443976FD27F991EC67D95E5F4E96B127CAB2396A94D6E45CDA44CA4C4867570D6B07542F8D4BF9FF97975DB9891515E66F525D2B3CBEB6D662BFB6C3F338E93B02142BFC44173A3764C56AADD202075B26DC2F9F7D7AE74BD7D00FD05EE430032663D27A57DF040103DF031403BB335A8549A03B87AB089D006F60852E4B8060");
									emvModule.setCAPK("9F0605A0000003339F220103DF05083230353031323331DF060101DF070101DF0281B0B0627DEE87864F9C18C13B9A1F025448BF13C58380C91F4CEBA9F9BCB214FF8414E9B59D6ABA10F941C7331768F47B2127907D857FA39AAF8CE02045DD01619D689EE731C551159BE7EB2D51A372FF56B556E5CB2FDE36E23073A44CA215D6C26CA68847B388E39520E0026E62294B557D6470440CA0AEFC9438C923AEC9B2098D6D3A1AF5E8B1DE36F4B53040109D89B77CAFAF70C26C601ABDF59EEC0FDC8A99089140CD2E817E335175B03B7AA33DDF040103DF031487F0CD7C0E86F38F89A66F8C47071A8B88586F26");
									emvModule.setCAPK("9F0605A0000003339F220104DF05083230353031323331DF060101DF070101DF0281F8BC853E6B5365E89E7EE9317C94B02D0ABB0DBD91C05A224A2554AA29ED9FCB9D86EB9CCBB322A57811F86188AAC7351C72BD9EF196C5A01ACEF7A4EB0D2AD63D9E6AC2E7836547CB1595C68BCBAFD0F6728760F3A7CA7B97301B7E0220184EFC4F653008D93CE098C0D93B45201096D1ADFF4CF1F9FC02AF759DA27CD6DFD6D789B099F16F378B6100334E63F3D35F3251A5EC78693731F5233519CDB380F5AB8C0F02728E91D469ABD0EAE0D93B1CC66CE127B29C7D77441A49D09FCA5D6D9762FC74C31BB506C8BAE3C79AD6C2578775B95956B5370D1D0519E37906B384736233251E8F09AD79DFBE2C6ABFADAC8E4D8624318C27DAF1DF040103DF0314F527081CF371DD7E1FD4FA414A665036E0F5E6E5");
									emvModule.setCAPK("9F0605A0000000039F220101DF05083230353031323331DF060101DF070101DF028180C696034213D7D8546984579D1D0F0EA519CFF8DEFFC429354CF3A871A6F7183F1228DA5C7470C055387100CB935A712C4E2864DF5D64BA93FE7E63E71F25B1E5F5298575EBE1C63AA617706917911DC2A75AC28B251C7EF40F2365912490B939BCA2124A30A28F54402C34AECA331AB67E1E79B285DD5771B5D9FF79EA630B75DF040103DF0314D34A6A776011C7E7CE3AEC5F03AD2F8CFC5503CC");
									emvModule.setCAPK("9F0605A0000000039F220107DF05083230353031323331DF060101DF070101DF028190A89F25A56FA6DA258C8CA8B40427D927B4A1EB4D7EA326BBB12F97DED70AE5E4480FC9C5E8A972177110A1CC318D06D2F8F5C4844AC5FA79A4DC470BB11ED635699C17081B90F1B984F12E92C1C529276D8AF8EC7F28492097D8CD5BECEA16FE4088F6CFAB4A1B42328A1B996F9278B0B7E3311CA5EF856C2F888474B83612A82E4E00D0CD4069A6783140433D50725FDF040103DF0314B4BC56CC4E88324932CBC643D6898F6FE593B172");
									emvModule.setCAPK("9F0605A0000000049F220103DF05083230353031323331DF060101DF070101DF028180C2490747FE17EB0584C88D47B1602704150ADC88C5B998BD59CE043EDEBF0FFEE3093AC7956AD3B6AD4554C6DE19A178D6DA295BE15D5220645E3C8131666FA4BE5B84FE131EA44B039307638B9E74A8C42564F892A64DF1CB15712B736E3374F1BBB6819371602D8970E97B900793C7C2A89A4A1649A59BE680574DD0B60145DF040103DF03145ADDF21D09278661141179CBEFF272EA384B13BB");
									emvModule.setCAPK("9F0605A0000000039F220108DF05083230353031323331DF060101DF070101DF0281B0D9FD6ED75D51D0E30664BD157023EAA1FFA871E4DA65672B863D255E81E137A51DE4F72BCC9E44ACE12127F87E263D3AF9DD9CF35CA4A7B01E907000BA85D24954C2FCA3074825DDD4C0C8F186CB020F683E02F2DEAD3969133F06F7845166ACEB57CA0FC2603445469811D293BFEFBAFAB57631B3DD91E796BF850A25012F1AE38F05AA5C4D6D03B1DC2E568612785938BBC9B3CD3A910C1DA55A5A9218ACE0F7A21287752682F15832A678D6E1ED0BDF040103DF031420D213126955DE205ADC2FD2822BD22DE21CF9A8");
									emvModule.setCAPK("9F0605A0000000049F220104DF05083230353031323331DF060101DF070101DF028190A6DA428387A502D7DDFB7A74D3F412BE762627197B25435B7A81716A700157DDD06F7CC99D6CA28C2470527E2C03616B9C59217357C2674F583B3BA5C7DCF2838692D023E3562420B4615C439CA97C44DC9A249CFCE7B3BFB22F68228C3AF13329AA4A613CF8DD853502373D62E49AB256D2BC17120E54AEDCED6D96A4287ACC5C04677D4A5A320DB8BEE2F775E5FEC5DF040103DF0314381A035DA58B482EE2AF75F4C3F2CA469BA4AA6C");
									emvModule.setCAPK("9F0605A0000000039F220109DF05083230353031323331DF060101DF070101DF0281F89D912248DE0A4E39C1A7DDE3F6D2588992C1A4095AFBD1824D1BA74847F2BC4926D2EFD904B4B54954CD189A54C5D1179654F8F9B0D2AB5F0357EB642FEDA95D3912C6576945FAB897E7062CAA44A4AA06B8FE6E3DBA18AF6AE3738E30429EE9BE03427C9D64F695FA8CAB4BFE376853EA34AD1D76BFCAD15908C077FFE6DC5521ECEF5D278A96E26F57359FFAEDA19434B937F1AD999DC5C41EB11935B44C18100E857F431A4A5A6BB65114F174C2D7B59FDF237D6BB1DD0916E644D709DED56481477C75D95CDD68254615F7740EC07F330AC5D67BCD75BF23D28A140826C026DBDE971A37CD3EF9B8DF644AC385010501EFC6509D7A41DF040103DF03141FF80A40173F52D7D27E0F26A146A1C8CCB29046");
									emvModule.setCAPK("9F0605A0000000049F220105DF05083230353031323331DF060101DF070101DF0281B0B8048ABC30C90D976336543E3FD7091C8FE4800DF820ED55E7E94813ED00555B573FECA3D84AF6131A651D66CFF4284FB13B635EDD0EE40176D8BF04B7FD1C7BACF9AC7327DFAA8AA72D10DB3B8E70B2DDD811CB4196525EA386ACC33C0D9D4575916469C4E4F53E8E1C912CC618CB22DDE7C3568E90022E6BBA770202E4522A2DD623D180E215BD1D1507FE3DC90CA310D27B3EFCCD8F83DE3052CAD1E48938C68D095AAC91B5F37E28BB49EC7ED597DF040103DF0314EBFA0D5D06D8CE702DA3EAE890701D45E274C845");
									emvModule.setCAPK("9F0605A0000000049F220106DF05083230353031323331DF060101DF070101DF0281F8CB26FC830B43785B2BCE37C81ED334622F9622F4C89AAE641046B2353433883F307FB7C974162DA72F7A4EC75D9D657336865B8D3023D3D645667625C9A07A6B7A137CF0C64198AE38FC238006FB2603F41F4F3BB9DA1347270F2F5D8C606E420958C5F7D50A71DE30142F70DE468889B5E3A08695B938A50FC980393A9CBCE44AD2D64F630BB33AD3F5F5FD495D31F37818C1D94071342E07F1BEC2194F6035BA5DED3936500EB82DFDA6E8AFB655B1EF3D0D7EBF86B66DD9F29F6B1D324FE8B26CE38AB2013DD13F611E7A594D675C4432350EA244CC34F3873CBA06592987A1D7E852ADC22EF5A2EE28132031E48F74037E3B34AB747FDF040103DF0314F910A1504D5FFB793D94F3B500765E1ABCAD72D9");
									emvModule.setCAPK("9F0605A0000003339F220108DF050420301231DF060101DF070101DF028190B61645EDFD5498FB246444037A0FA18C0F101EBD8EFA54573CE6E6A7FBF63ED21D66340852B0211CF5EEF6A1CD989F66AF21A8EB19DBD8DBC3706D135363A0D683D046304F5A836BC1BC632821AFE7A2F75DA3C50AC74C545A754562204137169663CFCC0B06E67E2109EBA41BC67FF20CC8AC80D7B6EE1A95465B3B2657533EA56D92D539E5064360EA4850FED2D1BFDF040103DF0314EE23B616C95C02652AD18860E48787C079E8E85A");
									emvModule.setCAPK("9F0605A0000003339F220109DF05083230333031323331DF060101DF070101DF0281B0EB374DFC5A96B71D2863875EDA2EAFB96B1B439D3ECE0B1826A2672EEEFA7990286776F8BD989A15141A75C384DFC14FEF9243AAB32707659BE9E4797A247C2F0B6D99372F384AF62FE23BC54BCDC57A9ACD1D5585C303F201EF4E8B806AFB809DB1A3DB1CD112AC884F164A67B99C7D6E5A8A6DF1D3CAE6D7ED3D5BE725B2DE4ADE23FA679BF4EB15A93D8A6E29C7FFA1A70DE2E54F593D908A3BF9EBBD760BBFDC8DB8B54497E6C5BE0E4A4DAC29E5DF040103DF0314A075306EAB0045BAF72CDD33B3B678779DE1F527");
									emvModule.setCAPK("9F0605A0000003339F22010ADF05083230333031323331DF060101DF070101DF028180B2AB1B6E9AC55A75ADFD5BBC34490E53C4C3381F34E60E7FAC21CC2B26DD34462B64A6FAE2495ED1DD383B8138BEA100FF9B7A111817E7B9869A9742B19E5C9DAC56F8B8827F11B05A08ECCF9E8D5E85B0F7CFA644EFF3E9B796688F38E006DEB21E101C01028903A06023AC5AAB8635F8E307A53AC742BDCE6A283F585F48EFDF040103DF0314C88BE6B2417C4F941C9371EA35A377158767E4E3");
									emvModule.setCAPK("9F0605A0000003339F22010BDF05083230333031323331DF060101DF070101DF0281F8CF9FDF46B356378E9AF311B0F981B21A1F22F250FB11F55C958709E3C7241918293483289EAE688A094C02C344E2999F315A72841F489E24B1BA0056CFAB3B479D0E826452375DCDBB67E97EC2AA66F4601D774FEAEF775ACCC621BFEB65FB0053FC5F392AA5E1D4C41A4DE9FFDFDF1327C4BB874F1F63A599EE3902FE95E729FD78D4234DC7E6CF1ABABAA3F6DB29B7F05D1D901D2E76A606A8CBFFFFECBD918FA2D278BDB43B0434F5D45134BE1C2781D157D501FF43E5F1C470967CD57CE53B64D82974C8275937C5D8502A1252A8A5D6088A259B694F98648D9AF2CB0EFD9D943C69F896D49FA39702162ACB5AF29B90BADE005BC157DF040103DF0314BD331F9996A490B33C13441066A09AD3FEB5F66C");
									emvModule.SyncCAPKFile();


									try {
										emvModule.setAID("9F0607A0000000031010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF180100", true);
										emvModule.setAID("9F0607A0000000032010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF180100", true);
										emvModule.setAID("9F0607A0000000033010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF180100", true);
										emvModule.setAID("9F0607A0000000041010DF0101009F08020002DF1105FC5080A000DF1205F85080F800DF130504000000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF180100", true);
										emvModule.setAID("9F0607A0000000043060DF0101009F08020002DF1105FC5058A000DF1205F85058F800DF130504000000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF180101", true);
										emvModule.setAID("9F0607A0000000651010DF0101009F08020200DF1105FC6024A800DF1205FC60ACF800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF180100", true);
										emvModule.setAID("9F0608A000000333010101DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000100000", true);
										emvModule.setAID("9F0608A000000333010102DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000100000", true);
										emvModule.setAID("9F0608A000000333010103DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000100000", true);
										emvModule.setAID("9F0608A000000333010106DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000100000", true);
									} catch (Exception e) {
										ToastUtils.show(context, "设置默认EMV参数异常!!!");
										e.printStackTrace();
									}
									emvModule.SyncAIDFile();
									//参数文件更新后需要重新建立AID候选列表
									emvModule.buildAIDList();
									ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_AID_DOWN, false);
									ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CAPK_DOWN, false);

								}
							}).start();
						}
					});

		} else {
		}
	}

	@Override
	public BaseBean getBean() {
		return null;
	}

	@Override
	protected void initEvent() {
		
	}
	
	@Override
	public void onFragmentHide() {
		if (dialog!=null && dialog.isShowing()){
			dialog.dismiss();
			dialog = null;
		}
		super.onFragmentHide();
	}

}
