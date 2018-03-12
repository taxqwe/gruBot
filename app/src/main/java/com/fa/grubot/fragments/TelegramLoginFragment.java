package com.fa.grubot.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fa.grubot.LoginActivity;
import com.fa.grubot.R;
import com.fa.grubot.abstractions.TelegramLoginFragmentBase;
import com.fa.grubot.presenters.TelegramLoginPresenter;
import com.fa.grubot.util.Globals;
import com.fa.grubot.util.TmApiStorage;
import com.github.badoualy.telegram.api.Kotlogram;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.tl.api.auth.TLSentCode;
import com.github.badoualy.telegram.tl.exception.RpcErrorException;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

import static com.fa.grubot.App.INSTANCE;

public class TelegramLoginFragment extends Fragment implements TelegramLoginFragmentBase {
    @Nullable @BindView(R.id.phoneNumber) EditText phoneNumber;
    @Nullable @BindView(R.id.continueBtn) Button continueBtn;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private TelegramLoginPresenter presenter;
    private Unbinder unbinder;

    public static TelegramLoginFragment newInstance() {
        Bundle args = new Bundle();
        TelegramLoginFragment fragment = new TelegramLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new TelegramLoginPresenter(this);
        View v = inflater.inflate(R.layout.fragment_telegram_login, container, false);
        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, v);

        presenter.notifyFragmentStarted();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
    }

    public void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Telegram вход");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setupViews() {
        continueBtn.setOnClickListener(v -> {
            if (!phoneNumber.getText().toString().isEmpty())
                new SendAuthMessageAsyncTask(getActivity(), phoneNumber.getText().toString()).execute();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class SendAuthMessageAsyncTask extends AsyncTask<Void, Void, Object> {
        private WeakReference<Context> context;
        private String phoneNumber;
        private MaterialDialog loadingDialog;

        private SendAuthMessageAsyncTask(Context context, String phoneNumber) {
            this.context = new WeakReference<>(context);
            this.phoneNumber = phoneNumber;
        }

        @Override
        protected void onPreExecute() {
            if (loadingDialog == null || !loadingDialog.isShowing()) {
                loadingDialog = Globals.getLoadingDialog(context.get());
                loadingDialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Void... params) {
            Object returnObject;

            TelegramClient client = INSTANCE.getTelegramClient();

            if (client.isClosed())
                client = INSTANCE.getNewTelegramClient();

            try {
                returnObject = client.authSendCode(false, phoneNumber, true);
                INSTANCE.setTelegramClient(client);
            } catch (RpcErrorException e) {
                returnObject = e;
            } catch (Exception e) {
                returnObject = e;
            } finally {
                //client.close();
            }

            return returnObject;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }

            if (result instanceof Exception) {
                Toast.makeText(context.get(), "Ошибка: " + ((Exception) result).getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Fragment telegramVerificationFragment = TelegramVerificationFragment.newInstance((TLSentCode) result, phoneNumber);

                FragmentManager fm = ((LoginActivity) context.get()).getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.content, telegramVerificationFragment);
                transaction.commit();
            }
            super.onPostExecute(result);
        }
    }
}
