package uralchem.kckk.uit;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author dmitrykunin
 *
 */
public class DialogFactory {
	protected DialogFactory() {};

	//���������, ����������� ��������� ���� ��������
	public final static int DIALOG_ALERT = 0;
	public final static int DIALOG_PROGRESS_1 = 1;
	public final static int DIALOG_INPUT = 2;
	public final static int DIALOG_PROGRESS_2 = 3;
	public final static int DIALOG_PROGRESS_3 = 4;
	public final static int DIALOG_PROGRESS_4 = 5;
	
	public static Dialog getDialogById(int id, String txt, final Context context, DialogInterface.OnClickListener onClickListener) 
	{

		Dialog dialog = null;
		switch (id) {
		case DIALOG_ALERT:
			dialog = createAlertDialog(context, onClickListener);
			break;
		case DIALOG_PROGRESS_1:

		   dialog = createProgressDialog(context);
		  ((AlertDialog) dialog).setMessage("�������� ������ � ����..");
		  ((AlertDialog) dialog).setCancelable(false);
			break;
		case DIALOG_INPUT:

		//	dialog = createInputAlert(context);
			break;
		case DIALOG_PROGRESS_2:

			   dialog = createProgressDialog(context);
			   ((AlertDialog) dialog).setMessage("��������..");
			   ((AlertDialog) dialog).setCancelable(false);
				break;
		case DIALOG_PROGRESS_3:

			   dialog = createProgressDialog(context);
			  ((AlertDialog) dialog).setMessage("����������..");
			  ((AlertDialog) dialog).setCancelable(false);
				break;
		case DIALOG_PROGRESS_4:
			   dialog = createProgressDialog(context);
			  ((AlertDialog) dialog).setMessage("��������..");
			  ((AlertDialog) dialog).setCancelable(false); 

				break;
			
		}
		return dialog;
	}
	

	private static Dialog createAlertDialog(final Context context, DialogInterface.OnClickListener onClickListener) {
		
		TextView view = new TextView(context);
	    view.setText("�� ����������� ��������� WIFI ������. \n������������� � ����� ������ ����� �� ��������. " +
				"\n" +
				"\n����������?" +
				"\n");
	    
		Dialog dialog;
		Builder builder = new AlertDialog.Builder(context);
		dialog = builder.setTitle("Alert dialog")
						.setPositiveButton("��", onClickListener)
						.setNegativeButton("���", onClickListener)
						//.setMessage("�� ����������� ��������� WIFI ������. \n������������� � ����� ������ ����� �� ��������. " +
						//		"\n" +
						//		"\n����������?").create();	
						.setView(view).create();
		view.setGravity(Gravity.CENTER);
	    view.setTextColor(Color.WHITE);
	    view.setTextSize(18);

		return dialog;
	}
	
	public static Dialog createProgressDialog(final Context context)
	{
         ProgressDialog dialog = new ProgressDialog(context);
         dialog.setCancelable(true);
         return dialog;         
	}
	

}
