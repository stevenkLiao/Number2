package com.example.user.nummachine2;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CallFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallFragment extends Fragment {

    Button conBtn, takeBtn;
    private OnFragmentInteractionListener mListener;

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener conBtntouch = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                conBtn.setBackgroundResource(R.drawable.callbtndown);
                conBtn.setTextColor(Color.parseColor("#dddddd"));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                conBtn.setBackgroundResource(R.drawable.callbtn);
                conBtn.setTextColor(Color.parseColor("#000000"));
            }
            return false;
        }
    };

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener takeBtntouch = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                takeBtn.setBackgroundResource(R.drawable.callbtndown);
                takeBtn.setTextColor(Color.parseColor("#dddddd"));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                takeBtn.setBackgroundResource(R.drawable.callbtn);
                takeBtn.setTextColor(Color.parseColor("#000000"));
            }
            return false;
        }
    };

    public CallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CallFragment newInstance(String param1, String param2) {
        CallFragment fragment = new CallFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_call, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // 在這個方法中取得並定義Fragment的介面元件
        super.onActivityCreated(savedInstanceState);
        conBtn = (Button) getView().findViewById(R.id.button4);
        takeBtn = (Button) getView().findViewById(R.id.button6);
        //按鈕變色
        conBtn.setOnTouchListener(conBtntouch);
        takeBtn.setOnTouchListener(takeBtntouch);
        //...
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
