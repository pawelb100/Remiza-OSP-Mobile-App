package edu.wseiz.remizaosp.fragments.main;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import edu.wseiz.remizaosp.adapters.MessageListAdapter;
import edu.wseiz.remizaosp.databinding.FragmentChatBinding;
import edu.wseiz.remizaosp.interfaces.OnItemListClick;
import edu.wseiz.remizaosp.listeners.FetchCurrentUserListener;
import edu.wseiz.remizaosp.listeners.FetchMessagesListener;
import edu.wseiz.remizaosp.listeners.UpdateListener;
import edu.wseiz.remizaosp.models.Message;
import edu.wseiz.remizaosp.models.Role;
import edu.wseiz.remizaosp.models.User;
import edu.wseiz.remizaosp.viewmodels.Repository;


public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;

    private Repository repository;

    private MessageListAdapter adapter;

    private User currentUser;

    private DateFormat fullDateFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        repository = viewModelProvider.get(Repository.class);

        fullDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");

        repository.singleFetchCurrentUser(new FetchCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                currentUser = user;
            }

            @Override
            public void onFailed() {
                handleFail();
            }
        });

        binding.btnSend.setOnClickListener(v -> {

            if (!(binding.etTypeNewMessage.getText().toString().isEmpty()))
            {
                    Message message = new Message();
                    message.setText(binding.etTypeNewMessage.getText().toString());
                    message.setUserId(repository.getUserId());
                    message.setAuthor(currentUser.getName());
                    message.setTimestamp(System.currentTimeMillis());

                    if (adapter.getItemCount()>10)
                        repository.deleteMessage(adapter.getLastMessageId(), new UpdateListener() {
                            @Override
                            public void onSuccess() {}

                            @Override
                            public void onFailed() {}
                        });

                    repository.sendMessage(message, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            binding.etTypeNewMessage.setText("");
                        }

                        @Override
                        public void onFailed() {
                            info("Nie udało się wysłać wiadomości");
                        }
                    });

                binding.rvMessages.smoothScrollToPosition(adapter.getItemCount()+1);


            }
        });

        repository.fetchMessages(new FetchMessagesListener() {
            @Override
            public void onSuccess(List<Message> messageList) {
                setAdapter(messageList);
            }

            @Override
            public void onFailed() {
                handleFail();
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repository.removeChatListener();
        binding = null;
    }

    private void setAdapter(List<Message> messageList) {

        if (adapter==null) {

            adapter = new MessageListAdapter(getContext(), messageList, repository.getUserId(), new OnItemListClick() {
                @Override
                public void onItemClick(Object item) {

                    Message message = (Message) item;

                    new AlertDialog.Builder(getContext())
                            .setTitle(message.getAuthor())
                            .setMessage(fullDateFormat.format(message.getTimestamp()))
                            .setNeutralButton("Ok", null)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }

                @Override
                public void onItemLongClick(Object item, View view) {

                    if (currentUser.getRole()==Role.Officer) {
                        Message message = (Message) item;
                        new AlertDialog.Builder(getContext())
                                .setTitle("Usunąć tę wiadomość?")
                                .setMessage(message.getText())
                                .setPositiveButton("Tak", (dialog, which) -> repository.deleteMessage(message.getId(), new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        info("Wiadomość usunięta");
                                    }

                                    @Override
                                    public void onFailed() {
                                        handleFail();
                                    }
                                }))
                                .setNegativeButton("Anuluj", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            });
            binding.rvMessages.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setStackFromEnd(true);
            binding.rvMessages.setLayoutManager(layoutManager);
            binding.rvMessages.smoothScrollToPosition(adapter.getItemCount());
        }
        else
        {
            adapter.updateData(messageList);
            binding.rvMessages.smoothScrollToPosition(adapter.getItemCount()+1);
        }
    }

    private void info(String text) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void handleFail() {
        info("Wystąpił problem z połączeniem");
    }

}