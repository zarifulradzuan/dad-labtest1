import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class GuiSwing {

	private JFrame frame;
	private JTextField txtStudentNo;
	private JTextField txtStudentName;
	private JTextField txtDataCount;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiSwing window = new GuiSwing();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GuiSwing() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnGetFromWeb = new JButton("Get From Web Service");
		btnGetFromWeb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread1 = new Thread(){
					public void run(){
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("selectFn", "readDatabase"));
						
						
						String strUrl = "http://rku.utem.edu.my/webServiceJSON/jsonwebServices.php";
						JSONArray jArr = makeHttpRequest(strUrl,"POST",params);
						JSONObject jsnObj=null;
						String strSetText="";
						int femaleCount=0;
						try {
							for(int i = 0 ;i<jArr.length();i++) {
								jsnObj = jArr.getJSONObject(i);
								String userID = jsnObj.getString("USER_ID");
								String healthFacilityCode = jsnObj.getString("HEALTH_FACILITY_CODE");
								String password = jsnObj.getString("PASSWORD");
								String username = jsnObj.get("USER_NAME").toString();
								String occupationCode = jsnObj.get("OCCUPATION_CODE").toString();
								String birthDate = jsnObj.get("BIRTH_DATE").toString();
								String sexCode = jsnObj.get("SEX_CODE").toString();
								if(sexCode.equalsIgnoreCase("Female"))
									femaleCount++;
								String newICNo = jsnObj.get("NEW_ICNO").toString();
								String homePhone = jsnObj.get("HOME_PHONE").toString();
								String officePhone = jsnObj.get("OFFICE_PHONE").toString();
								String mobilePhone = jsnObj.get("MOBILE_PHONE").toString();
								String loginStatus = jsnObj.get("LOGIN_STATUS").toString();
								String userStatus = jsnObj.get("USER_STATUS").toString();
								String email = jsnObj.get("E_MAIL").toString();
								String idCategoryCode = jsnObj.get("ID_CATEGORY_CODE").toString();
								String startDate = jsnObj.get("START_DATE").toString();
								String endDate = jsnObj.get("END_DATE").toString();
								String roomNo = jsnObj.get("ROOM_NO").toString();
								String hfcCd = jsnObj.get("hfc_cd").toString();
								strSetText += "User ID :"+userID+
										" || Health Facility Code :"+healthFacilityCode+
										" || Password :"+password+
										" || Username :"+username+
										" || Password :"+occupationCode+
										" || Birth Date :"+birthDate+
										" || Sex Code :"+sexCode+
										" || New IC No :"+newICNo+
										" || Home Phone :"+homePhone+
										" || Office Phone :"+officePhone+
										" || Mobile Phone :"+mobilePhone+
										" || Login Status :"+loginStatus+
										" || User Status :"+userStatus+
										" || Email :"+email+
										" || ID Category Code :"+idCategoryCode+
										" || Start Date :"+startDate+
										" || End Date :"+endDate+
										" || Room No :"+roomNo+
										" || hfc cd :"+hfcCd+"\n";
							}
							textArea.setText(strSetText);
							txtDataCount.setText(String.valueOf(femaleCount));
					
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					public JSONArray makeHttpRequest(String strUrl, String method, List<NameValuePair> params) {
						InputStream is = null;
						String json = "";
						JSONArray jArr = null;
						
						try {
							if(method == "POST") {
								DefaultHttpClient httpClient = new DefaultHttpClient();
								HttpPost httpPost = new HttpPost(strUrl);
								httpPost.setEntity(new UrlEncodedFormEntity(params));
								HttpResponse httpResponse = httpClient.execute(httpPost);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							else if(method == "GET") {
								DefaultHttpClient httpClient = new DefaultHttpClient();
								String paramString = URLEncodedUtils.format(params, "utf-8");
								strUrl+="?"+paramString;
								HttpGet httpGet = new HttpGet(strUrl);
								
								HttpResponse httpResponse = httpClient.execute(httpGet);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							
							BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
							StringBuilder sb = new StringBuilder();
							String line = null;
							while((line = reader.readLine())!=null) 
								sb.append(line+"\n");
							is.close();
							json = sb.toString();
							jArr = new JSONArray(json);
							
						}	catch(JSONException e) {
							try {
								jArr = new JSONArray(json);
							}catch(JSONException e1) {
								e1.printStackTrace();
							}
						}	catch (Exception ee) {
							ee.printStackTrace();
						}
						return jArr;
					}
				};
				thread1.start();
			}
		});
		btnGetFromWeb.setBounds(124, 11, 175, 23);
		frame.getContentPane().add(btnGetFromWeb);
		
		JLabel lblDataFromWeb = new JLabel("Data From Web Service");
		lblDataFromWeb.setBounds(10, 63, 152, 14);
		frame.getContentPane().add(lblDataFromWeb);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 87, 392, 88);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JLabel lblStudentNo = new JLabel("Student No:");
		lblStudentNo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStudentNo.setBounds(10, 186, 85, 14);
		frame.getContentPane().add(lblStudentNo);
		
		JLabel lblStudentName = new JLabel("Student Name:");
		lblStudentName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStudentName.setBounds(10, 212, 85, 14);
		frame.getContentPane().add(lblStudentName);
		
		JLabel lblDataCount = new JLabel("Data Count:");
		lblDataCount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDataCount.setBounds(10, 237, 85, 14);
		frame.getContentPane().add(lblDataCount);
		
		txtStudentNo = new JTextField();
		txtStudentNo.setBounds(96, 183, 152, 20);
		frame.getContentPane().add(txtStudentNo);
		txtStudentNo.setColumns(10);
		
		txtStudentName = new JTextField();
		txtStudentName.setBounds(96, 209, 152, 20);
		frame.getContentPane().add(txtStudentName);
		txtStudentName.setColumns(10);
		
		txtDataCount = new JTextField();
		txtDataCount.setBounds(96, 234, 86, 20);
		frame.getContentPane().add(txtDataCount);
		txtDataCount.setColumns(10);
		
		JButton btnVerify = new JButton("Verify");
		btnVerify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread2 = new Thread(){
					public void run(){
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("selectFn", "insertStud"));
						params.add(new BasicNameValuePair("stud_name",txtStudentName.getText()));
						params.add(new BasicNameValuePair("stud_no",txtStudentNo.getText()));
						params.add(new BasicNameValuePair("varAns",txtDataCount.getText()));
						String strUrl = "http://rku.utem.edu.my/webServiceJSON/jsonwebServices.php";
						JSONObject jObj = makeHttpRequest(strUrl,"POST",params);
						try {
							JOptionPane.showMessageDialog(frame, jObj.getString("respond"));
						} catch (HeadlessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					public JSONObject makeHttpRequest(String strUrl, String method, List<NameValuePair> params) {
						InputStream is = null;
						String json = "";
						JSONObject jObj = null;
						
						try {
							if(method == "POST") {
								DefaultHttpClient httpClient = new DefaultHttpClient();
								HttpPost httpPost = new HttpPost(strUrl);
								httpPost.setEntity(new UrlEncodedFormEntity(params));
								HttpResponse httpResponse = httpClient.execute(httpPost);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							else if(method == "GET") {
								DefaultHttpClient httpClient = new DefaultHttpClient();
								String paramString = URLEncodedUtils.format(params, "utf-8");
								strUrl+="?"+paramString;
								HttpGet httpGet = new HttpGet(strUrl);
								
								HttpResponse httpResponse = httpClient.execute(httpGet);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							
							BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
							StringBuilder sb = new StringBuilder();
							String line = null;
							while((line = reader.readLine())!=null) 
								sb.append(line+"\n");
							is.close();
							json = sb.toString();
							jObj = new JSONObject(json);
							
						}	catch(JSONException e) {
							try {
								JSONArray jArr = new JSONArray(json);
							}catch(JSONException e1) {
								e1.printStackTrace();
							}
						}	catch (Exception ee) {
							ee.printStackTrace();
						}
						return jObj;
					}
				};
				thread2.start();
			}
		});
		btnVerify.setBounds(323, 233, 89, 23);
		frame.getContentPane().add(btnVerify);
		
		
	}
	
	
}
