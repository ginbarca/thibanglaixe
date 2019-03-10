package ui.fragmentImpl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.AnswerAdapter;
import model.Answer;
import model.Question;
import ui.fragment.QuestionFragment;

public class QuestionFragmentImpl extends Fragment implements QuestionFragment {


    private ListView lvAnswer;
    private AnswerAdapter dapAnAdapter;
    private TextView txtCauHoi, txtMaCauHoi, txtKiemTra;
    private Button btnHienAnswer, btnAnAnswer;
    private CheckBox chkAnswer;
    private ImageView imgHinhCauHoi;
    private View view;


    private int macauhoi;
    private Question question;
    private String isTest;
    private String task;
    private boolean isRightAnswer;
    private byte[] imgsrc;
    private int position;
    private List<Answer> listAnswer = new ArrayList<>();
    private List<Integer> checkedAnswers = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.question_fragment, container, false);

//        questionPresenter = new QuestionPresenterImpl(QuestionFragmentImpl.this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addControls();
        addEvents();
    }

    public static QuestionFragmentImpl getInstance(Question question, String isTest, String task) {
        QuestionFragmentImpl myFragment = new QuestionFragmentImpl();
        myFragment.question = question;
        myFragment.macauhoi = question.getId();
        myFragment.isTest = isTest;
        myFragment.task = task;
        return myFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Toast.makeText(getContext(), "pause", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getContext(), "resume", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean isRightAnswer() {
        return isRightAnswer;
    }

    @Override
    public void setRightAnswer(boolean rightAnswer) {
        isRightAnswer = rightAnswer;
    }

    @Override
    public List<Integer> getCheckedAnswers() {
        return checkedAnswers;
    }

    @Override
    public void setCheckedAnswers(List<Integer> checkedAnswers) {
        this.checkedAnswers = checkedAnswers;
    }

    @Override
    public void addEvents() {
        hienThiAnswer();

        btnHienAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyAnHienAnswerDung(true);
            }
        });
        btnAnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyAnHienAnswerDung(false);
            }
        });

    }

    @Override
    public void hienThiAnswer() {
        // Load list cau hoi

        //Hiển thị hoặc ẩn ảnh biển báo/sa hình
        if (task.equals("sa_hinh")) {
            // TO-DO
//            imgsrc=cursorCH.getBlob(4);
//            Bitmap bitmap= BitmapFactory.decodeByteArray(imgsrc,0,imgsrc.length);
//            imgHinhCauHoi.setImageBitmap(bitmap);
            Picasso.with(QuestionFragmentImpl.this.getContext()).load("http://songmoi.vn/public/upload_editor/posts/images/angry-husky-christmas-6.jpg")
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imgHinhCauHoi, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            imgHinhCauHoi.setVisibility(View.GONE);
        }
//        listAnswer.clear();
        txtCauHoi.setText(question.getContent());

        // Load list đáp án
        listAnswer = question.getDapAn();
        Collections.shuffle(listAnswer);


        dapAnAdapter = new AnswerAdapter(QuestionFragmentImpl.this.getActivity(), R.layout.answer_item, listAnswer);
        lvAnswer.setAdapter(dapAnAdapter);
        dapAnAdapter.notifyDataSetChanged();

        // Ẩn nút ẩn/hiện đáp án nếu là câu hỏi thi
        if (isTest.equals("True")) {
            btnAnAnswer.setVisibility(View.GONE);
            btnHienAnswer.setVisibility(View.GONE);
            txtMaCauHoi.setText("Câu " + macauhoi % 100 + "");
        } else {
            txtMaCauHoi.setText("Câu " + macauhoi + "");
        }

    }

    @Override
    public void addControls() {
        lvAnswer = view.findViewById(R.id.lv_DapAn);
        txtMaCauHoi = view.findViewById(R.id.txtMaCauHoi);
        txtCauHoi = view.findViewById(R.id.txtCauHoi);
        btnHienAnswer = view.findViewById(R.id.btn_HienDapAn);
        btnAnAnswer = view.findViewById(R.id.btn_AnDapAn);
        imgHinhCauHoi = view.findViewById(R.id.imgHinhCauHoi);
    }

    @Override
    public boolean traLoiDung() {
        int sodapandung = 0;
        int sodapandungduocchon = 0;
        int sodapansai = 0;
        int sodapansaikhongduocchon = 0;
        for (int i = 0; i < listAnswer.size(); i++) {
            View dapan = lvAnswer.getChildAt(i);
            chkAnswer = dapan.findViewById(R.id.chkDapAn);
            if (listAnswer.get(i).getIsRight().equals("True")) {
                sodapandung++;
                if (chkAnswer.isChecked()) {
                    sodapandungduocchon++;
                    checkedAnswers.add(i);
                }
            } else {
                sodapansai++;
                if (!chkAnswer.isChecked()) {
                    sodapansaikhongduocchon++;
                }
            }
        }
        if (sodapandung == sodapandungduocchon && sodapansai == sodapansaikhongduocchon)
            return true;
        else
            return false;
    }

    @Override
    public void xuLyAnHienAnswerDung(boolean isHide) {
        for (int i = 0; i < listAnswer.size(); i++) {
            View dapan = lvAnswer.getChildAt(i);
            chkAnswer = dapan.findViewById(R.id.chkDapAn);
            txtKiemTra = dapan.findViewById(R.id.txtKiemTra);
            if (isHide) {
                if (listAnswer.get(i).getIsRight().equals("True")) {
                    txtKiemTra.setBackgroundColor(getResources().getColor(R.color.green));

                } else txtKiemTra.setBackgroundColor(getResources().getColor(R.color.red));
            } else {
                txtKiemTra.setBackgroundColor(getResources().getColor(R.color.white));
            }
//            if(chkAnswer.isChecked()) {
//                Toast.makeText(getContext(), i + "", Toast.LENGTH_SHORT).show();
//            }

        }

    }

    @Override
    public void xuLyLuuAnswerDuocChon() {
        for (int i = 0; i < checkedAnswers.size(); i++)
            Toast.makeText(getContext(), checkedAnswers.get(i) + "", Toast.LENGTH_SHORT).show();

    }
}
