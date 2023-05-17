package algonquin.cst2335.okay0003.data;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends ViewModel {

    public MutableLiveData<String> editString = new MutableLiveData<>();
    public MutableLiveData<Boolean> btn = new MutableLiveData<>();
    public MutableLiveData<Integer> imgHeight = new MutableLiveData<>();
    public MutableLiveData<Integer> imgWidth = new MutableLiveData<>();

}
