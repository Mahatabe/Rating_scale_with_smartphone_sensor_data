package com.example.sensorratingscale.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SlideshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue
                ("\nThis application is a part of the thesis research titled as \"Apply Machine Learning Algorithms to Predict Product\n" +
                        "Ratings Scale by Observing The Smartphone Sensors Data\". \n \n \n" +
                        "The Supervisor of this research is: \n" +
                        "Mr. H M Zabir Haque \n" +
                        "Assistant Professor, \n" + "Department of Computer Science and Engineering, AUST. \n \n" +
                        "The Contributors of this application are: \n" +
                        "[1]  180104074 \n - Mostafa Mahatabe \n \n"

                );
    }

    public LiveData<String> getText() {
        return mText;
    }
}
