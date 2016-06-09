package makdroid.servicesproject.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import makdroid.servicesproject.R;
import makdroid.servicesproject.model.FileModel;
import makdroid.servicesproject.utils.AlertDialogBuilder;
import makdroid.servicesproject.utils.FileUtil;


/**
 * Created by Grzecho on 22.11.2015.
 */
public class FileModelAdapter extends ArrayAdapter<FileModel> {

    private final List<FileModel> list;
    private final Activity context;

    public FileModelAdapter(Activity context, List<FileModel> list) {
        super(context, R.layout.list_view_files, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = context.getLayoutInflater();
            v = vi.inflate(R.layout.list_view_files, parent, false);
        }
        TextView tvFileName = (TextView) v.findViewById(R.id.tv_file_name);
        tvFileName.setText(list.get(position).getName());

        Button bDelete = (Button) v.findViewById(R.id.btn_delete);

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = FileUtil.getAppDir(getContext());
                File f = new File(dir, list.get(position).getName());
                f.delete();
                list.remove(position);
                Toast.makeText(context, context.getString(R.string.result_delete_file), Toast.LENGTH_LONG).show();
                notifyDataSetChanged();
            }
        });

        Button bOpen = (Button) v.findViewById(R.id.btn_open);

        bOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = FileUtil.getAppDir(getContext());
                File file = new File(dir, list.get(position).getName());
                String type = FileUtil.getMimeType(file.getPath());

                if (type == null)
                    type = "*/*";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), type);

                try {
                    getContext().startActivity(intent);
                } catch (ActivityNotFoundException activityNotFound) {
                    AlertDialog alertDialog = AlertDialogBuilder.createErrorDialog(getContext().getString(R.string.error_start_intent), getContext());
                    alertDialog.show();
                }
            }
        });

        return v;
    }


}
