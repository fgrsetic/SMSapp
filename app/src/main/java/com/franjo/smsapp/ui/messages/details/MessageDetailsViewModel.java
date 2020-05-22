package com.franjo.smsapp.ui.messages.details;

//public class MessageDetailsViewModel extends AndroidViewModel {
//
//    private IConversationDataSource conversationDataSource;
//    private MutableLiveData<List<Message>> databaseConversationList;
//
//    public MessageDetailsViewModel(Application application) {
//        super(application);
//        conversationDataSource = new DatabaseConversationsDataSource(application);
//    }
//
//    public LiveData<List<Message>> observeConversationsList() {
//        if (databaseConversationList == null) {
//            databaseConversationList = new MutableLiveData<>();
//        }
//        return databaseConversationList;
//    }
//
//    void setMessagesList(String phoneNumber) {
//        AppExecutors.getInstance().diskIO().execute(() -> {
//            conversationDataSource.getAllMessagesByPhoneNumber();
//         //   List<Message> conversationsList = conversationDataSource.getAllMessagesByPhoneNumber(phoneNumber);
//           // databaseConversationList.postValue(conversationsList);
//        });
//    }
//}
