package com.hero_state.hero_state_app;

import java.util.Arrays;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView.HitTestResult;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements StatusCallback,View.OnClickListener{

	private LoginButton btnLogin;
	private ProfilePictureView profile;
	private TextView userTextView;
	private UiLifecycleHelper uiHelper;
	private LinearLayout actionLayout;
	private EditText editText;
	private Button statusUpButton;
	//private Context context;

	private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, this);
        uiHelper.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
       // logKeyHash();
        
        //view matching
        btnLogin = (LoginButton) findViewById(R.id.mainLoginBtn);
        profile = (ProfilePictureView) findViewById(R.id.mainProfile);
        userTextView = (TextView) findViewById(R.id.maintext);
        actionLayout = (LinearLayout) findViewById(R.id.statusLayout);
        editText = (EditText) findViewById(R.id.mainstatusupdate);
        statusUpButton = (Button) findViewById(R.id.mainBtnUpdate);
        
        btnLogin.setPublishPermissions(Arrays.asList("publish_actions","publish_stream"));
        statusUpButton.setOnClickListener(this);
      }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	uiHelper.onResume();
    }
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	uiHelper.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	super.onSaveInstanceState(outState);
    	uiHelper.onSaveInstanceState(outState);
    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	uiHelper.onDestroy();
    	
    }@Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	uiHelper.onStop();
    }
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	uiHelper.onActivityResult(requestCode, resultCode, data);
   }
   @Override
   public void call(Session session, SessionState state, Exception exception) {
	// TODO Auto-generated method stub
	   if(session == null){
		   actionLayout.setVisibility(View.GONE);
		   profile.setProfileId(null);
		   userTextView.setText("Please Log In");
	   }else if(session.isClosed()){
		   actionLayout.setVisibility(View.GONE);
		   profile.setProfileId(null);
		userTextView.setText("Please Log In");
	   }else if(session.isOpened()){
		   actionLayout.setVisibility(View.VISIBLE);
		   Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
			
			@Override
			public void onCompleted(GraphUser user, Response response) {
				// TODO Auto-generated method stub
				if(user != null){
					profile.setProfileId(user.getId());
					userTextView.setText(user.getName());
				}
			}
		});
	   }
	
   }
   @SuppressWarnings("deprecation")
@Override


   public void onClick(View view) {
	   	// TODO Auto-generated method stub
	   if(view == statusUpButton){
		   String mess = editText.getText().toString().trim();
		   if(mess.length() == 0){
			   Toast.makeText(this,"Pleae Enter Your Message.", Toast.LENGTH_LONG).show();
			  
		   }else{
			   Session session = Session.getActiveSession();
			   Request.executeStatusUpdateRequestAsync(session, mess, new Request.Callback() {
				
				@Override
				public void onCompleted(Response response) {
					// TODO Auto-generated method stub
					//Toast toa = Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG);
					if(response.getError() == null){
						editText.setText(null);
						Toast.makeText(getBaseContext(),"Update Status Complete.",Toast.LENGTH_LONG).show();
						
					}else {
						Toast.makeText(getBaseContext(), response.getError().getErrorMessage(),Toast.LENGTH_LONG).show();
					}
				}
			});
		   }
	   }
   }
}


