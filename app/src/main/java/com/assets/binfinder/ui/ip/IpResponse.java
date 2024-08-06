package com.assets.binfinder.ui.ip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.assets.binfinder.R;
import com.assets.binfinder.databinding.FragmentIpResponseBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class IpResponse extends BottomSheetDialogFragment {
    FragmentIpResponseBinding binding;
    IpViewModel ipViewModel;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        binding = FragmentIpResponseBinding.inflate(inflater, container, false);
        ipViewModel = new ViewModelProvider(requireActivity()).get(IpViewModel.class);
        IpResponseArgs args = IpResponseArgs.fromBundle(requireArguments());
        ipViewModel.getIpData(args.getIp());
        ipViewModel.getSpamInfo(args.getIp());
        ipViewModel.getVulInfo(args.getIp());
        ipViewModel.getDatacenterInfo(args.getIp());


        ipViewModel.ip_info.observe(getViewLifecycleOwner(), event -> {
            if (!event.getHasBeenHandled()) {
                String ip_info = event.getContentIfNotHandled();
                if (ip_info != null) {
                    try {
                        binding.progressIndicator.hide();
                        JSONObject jsonObject = new JSONObject(ip_info);
                        binding.textViewIp.setText(jsonObject.getString("query"));
                        binding.country.setText(String.format(Locale.getDefault(), "%s (%s)", jsonObject.getString("country"), jsonObject.getString("countryCode")));
                        binding.state.setText(String.format(Locale.getDefault(), "%s (%s)", jsonObject.getString("regionName"), jsonObject.getString("region")));
                        binding.city.setText(jsonObject.getString("city"));
                        binding.zip.setText(jsonObject.getString("zip"));
                        binding.timezone.setText(jsonObject.getString("timezone"));
                        binding.asn.setText(jsonObject.getString("as"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


        });


        ipViewModel.vulnerability_info.observe(getViewLifecycleOwner(), event -> {
            if (!event.getHasBeenHandled()) {
                String s = event.getContentIfNotHandled();
                if (s != null) {
                    binding.progressIndicator.hide();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        try {
                            StringBuilder stringBuilder = new StringBuilder();
                            JSONArray jsonArray = jsonObject.getJSONArray("hostnames");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                stringBuilder.append(jsonArray.getString(i)).append(" , ");
                            }
                            binding.hostnames.setText(stringBuilder.toString());
                        } catch (Exception e) {
                            binding.hostnames.setText(null);
                        }
                        try {
                            binding.tags.setText(jsonObject.getJSONArray("tags").toString());
                        } catch (Exception e) {
                            binding.tags.setText(null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


        });


        ipViewModel.spam_info.observe(getViewLifecycleOwner(), event -> {

            if (!event.getHasBeenHandled()) {
                String s = event.getContentIfNotHandled();
                if (s != null) {
                    binding.progressIndicator.hide();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        try {
                            binding.frequency.setText(jsonObject.getJSONObject("ip").getString("frequency"));
                        } catch (Exception e) {
                            binding.frequency.setText(null);
                        }
                        try {
                            binding.appers.setText(jsonObject.getJSONObject("ip").getString("appears"));
                        } catch (Exception e) {
                            binding.appers.setText(null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        ipViewModel.datacenter_info.observe(getViewLifecycleOwner(), stringEvent -> {
            if (!stringEvent.getHasBeenHandled()) {

                String s = stringEvent.getContentIfNotHandled();
                if (s != null) {
                    binding.progressIndicator.hide();
                    try {
                        JSONObject datacenter = new JSONObject(s);
                        JSONObject asn = datacenter.getJSONObject("asn");

                        try {


                            if (datacenter.getString("is_abuser").equals("true")) {
                                binding.imgAbuse.setImageResource(R.drawable.ic_right);
                            } else {
                                binding.imgAbuse.setImageResource(R.drawable.ic_wrong);
                            }


                            binding.isAbuser.setText(datacenter.getString("is_abuser"));
                        } catch (Exception e) {
                            binding.isAbuser.setText(null);
                        }


                        try {


                            if (datacenter.getString("is_tor").equals("true")) {
                                binding.imgTor.setImageResource(R.drawable.ic_right);
                            } else {
                                binding.imgTor.setImageResource(R.drawable.ic_wrong);
                            }

                            binding.isTor.setText(datacenter.getString("is_tor"));
                        } catch (Exception e) {
                            binding.isTor.setText(null);
                        }


                        try {
                            if (datacenter.getString("is_proxy").equals("true")) {
                                binding.imgProxy.setImageResource(R.drawable.ic_right);
                            } else {
                                binding.imgProxy.setImageResource(R.drawable.ic_wrong);
                            }

                            binding.isProxy.setText(datacenter.getString("is_proxy"));
                        } catch (Exception e) {
                            binding.isProxy.setText(null);
                        }


                        try {

                            if (datacenter.getString("is_datacenter").equals("true")) {
                                binding.imgDatacenter.setImageResource(R.drawable.ic_right);
                            } else {
                                binding.imgDatacenter.setImageResource(R.drawable.ic_wrong);
                            }

                            binding.isDatacenter.setText(datacenter.getString("is_datacenter"));
                        } catch (Exception e) {
                            binding.isDatacenter.setText(null);
                        }


                        try {
                            binding.rir.setText(datacenter.getString("rir"));
                        } catch (Exception e) {
                            binding.rir.setText(null);
                        }


                        try {


                            try {
                                binding.org.setText(asn.getString("org"));
                            } catch (Exception e) {
                                binding.org.setText(null);
                            }

                            try {
                                binding.as.setText(asn.getString("asn"));
                            } catch (Exception e) {
                                binding.as.setText(null);
                            }
                            try {
                                binding.asnDescription.setText(asn.getString("descr"));
                            } catch (Exception e) {
                                binding.asnDescription.setText(null);
                            }


                            try {
                                binding.abuseEmail.setText(asn.getString("abuse"));
                            } catch (Exception e) {
                                binding.abuseEmail.setText(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}