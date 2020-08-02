package com.subhajitkar.commercial.project_neela.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.subhajitkar.commercial.project_neela.R;
import com.subhajitkar.commercial.project_neela.utils.NetworkManager;
import com.subhajitkar.commercial.project_neela.utils.StaticFields;

public class WebDialogFragment extends DialogFragment {
    private static final String TAG = "WebDialogFragment";

    private Toolbar toolbar;
    private WebView webView;
    private static String mWebUrl;
    private FrameLayout progressFrame;

    public static WebDialogFragment display(FragmentManager fragmentManager, String webUrl) {
        Log.d(TAG, "display: displaying dialog fragment");
        WebDialogFragment webDialog = new WebDialogFragment();
        webDialog.show(fragmentManager, TAG);
        mWebUrl = webUrl;

        return webDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        StaticFields.setStatusBarBackground(getActivity(), R.color.blue_5);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: gets called");
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_animateSlide);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_web, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //widgets init
        toolbar = view.findViewById(R.id.toolbar);
        webView = view.findViewById(R.id.webView);
        progressFrame = view.findViewById(R.id.progress_frame);
        //toolbar nav
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebDialogFragment.this.dismiss();
            }
        });
        toolbar.setTitle("");
        //webView
        if (new NetworkManager(getContext()).isNetworkConnected()) {
            webView.getSettings().setBuiltInZoomControls(true);   //setting up webView properties
            webView.getSettings().setDisplayZoomControls(false);
            webView.getSettings().setJavaScriptEnabled(true);  //in case of any error
            webView.setWebViewClient(new WebViewClient(){

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressFrame.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    toolbar.setTitle(view.getTitle());
                    toolbar.setSubtitle(view.getOriginalUrl());
                    toolbar.setLogo(R.drawable.ic_web_secure);
                    progressFrame.setVisibility(View.GONE);
                }
            });
            webView.loadUrl(mWebUrl);
        }
    }
}
