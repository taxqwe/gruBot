package com.fa.grubot.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.fa.grubot.App;
import com.fa.grubot.LoginActivity;
import com.fa.grubot.MainActivity;
import com.fa.grubot.R;
import com.fa.grubot.abstractions.TelegramVerificationFragmentBase;
import com.fa.grubot.objects.group.CurrentUser;
import com.fa.grubot.presenters.TelegramVerificationPresenter;
import com.fa.grubot.util.Globals;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.tl.api.TLUser;
import com.github.badoualy.telegram.tl.api.auth.TLAuthorization;
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

public class TelegramVerificationFragment extends Fragment implements TelegramVerificationFragmentBase {
    @Nullable @BindView(R.id.verificationCode) EditText verificationCode;
    @Nullable @BindView(R.id.continueBtn) Button continueBtn;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private TelegramVerificationPresenter presenter;
    private Unbinder unbinder;

    private TLSentCode sentCode;
    private String phoneNumber;

    public static TelegramVerificationFragment newInstance(TLSentCode sentCode, String phoneNumber) {
        Bundle args = new Bundle();
        args.putString("phoneNumber", phoneNumber);
        args.putSerializable("sentCode", sentCode);
        TelegramVerificationFragment fragment = new TelegramVerificationFragment();
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
        presenter = new TelegramVerificationPresenter(this);
        View v = inflater.inflate(R.layout.fragment_telegram_verification, container, false);
        setHasOptionsMenu(true);
        sentCode = (TLSentCode) this.getArguments().getSerializable("sentCode");
        phoneNumber = this.getArguments().getString("phoneNumber");
        unbinder = ButterKnife.bind(this, v);

        presenter.notifyFragmentStarted();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("TelegramHelper подтверждение");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setupViews() {
        continueBtn.setOnClickListener(v -> {
            if (!verificationCode.getText().toString().isEmpty())
                (new CheckAuthMessageAsyncTask(getActivity(), sentCode, phoneNumber, verificationCode.getText().toString())).execute();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
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

    private static class CheckAuthMessageAsyncTask extends AsyncTask<Void, Void, Object> {
        private WeakReference<Context> context;
        private TLSentCode sentCode;
        private String phoneNumber;
        private String verificationCode;
        private MaterialDialog loadingDialog;

        private CheckAuthMessageAsyncTask(Context context, TLSentCode sentCode, String phoneNumber, String verificationCode) {
            this.context = new WeakReference<>(context);
            this.sentCode = sentCode;
            this.phoneNumber = phoneNumber;
            this.verificationCode = verificationCode;
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

            TelegramClient client = INSTANCE.getNewTelegramClient(null);

            try {
                TLAuthorization authorization = client.authSignIn(phoneNumber, sentCode.getPhoneCodeHash(), verificationCode);
                returnObject = authorization.getUser().getAsUser();
            } catch (RpcErrorException e) {
                e.printStackTrace();
                returnObject = e;
            } catch (Exception e) {
                e.printStackTrace();
                returnObject = e;
            } finally {
                INSTANCE.closeTelegramClient();
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
                CurrentUser currentUser = App.INSTANCE.getCurrentUser();
                currentUser.setTelegramUser((TLUser) result);

                if (!currentUser.hasVkUser())
                    context.get().startActivity(new Intent(context.get(), MainActivity.class));
                ((LoginActivity) context.get()).finish();
            }
            super.onPostExecute(result);
        }
    }
}
