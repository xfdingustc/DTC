package com.nonda.dtc.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nonda.dtc.R;
import com.nonda.dtc.app.AppHolder;
import com.nonda.dtc.model.ObdData;
import com.nonda.dtc.rx.Transformers;
import com.nonda.dtc.ui.obd.FuelViewHolder;
import com.nonda.dtc.ui.obd.ObdDataHandler;
import com.nonda.dtc.ui.obd.ObdViewHolder;
import com.nonda.dtc.views.NumberAnimTextView;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by whaley on 2017/5/26.
 */

public class NormalFragment extends BaseFragment {

    private ObdDataHandler mObdDataHandler;

    @BindView(R.id.voltage)
    TextView voltage;

    @BindView(R.id.rmp)
    NumberAnimTextView rpm;

    @BindView(R.id.speed)
    NumberAnimTextView speed;

    @BindView(R.id.coolant)
    TextView coolant;

    @BindView(R.id.fuleLeve)
    TextView fuleLevel;

    @BindView(R.id.icon_fuel_level)
    ImageView iconFuelLeve;

    @BindView(R.id.lable_fuel_level)
    TextView labelFuelLevel;

    @BindView(R.id.instantMpg)
    TextView instantMpg;

    @BindView(R.id.averageMpg)
    NumberAnimTextView averageMpg;

    @BindView(R.id.range)
    TextView range;

    @BindView(R.id.fuel_unit)
    TextView fuelUnit;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = createFragmentView(inflater, container, R.layout.fragment_normal, savedInstanceState);
        initViews();
        return view;
    }

    private void initViews() {
        mObdDataHandler = new ObdDataHandler(new ObdViewHolder() {
            @Override
            public TextView getVoltage() {
                return voltage;
            }

            @Override
            public FuelViewHolder getFuelViewHolder() {
                return new FuelViewHolder() {
                    @Override
                    public TextView getFuelLevel() {
                        return fuleLevel;
                    }

                    @Override
                    public ImageView getFuelLevelIcon() {
                        return iconFuelLeve;
                    }

                    @Override
                    public TextView getFuelLevelLabel() {
                        return labelFuelLevel;
                    }

                    @Override
                    public TextView getFuelLevelUnit() {
                        return fuelUnit;
                    }
                };
            }



            @Override
            public NumberAnimTextView getRpm() {
                return rpm;
            }

            @Override
            public NumberAnimTextView getSpeed() {
                return speed;
            }

            @Override
            public TextView getCoolant() {
                return coolant;
            }

            @Override
            public TextView getInstantMpg() {
                return instantMpg;
            }

            @Override
            public NumberAnimTextView getAverageMpg() {
                return averageMpg;
            }

            @Override
            public TextView getRange() {
                return range;
            }
        });

        AppHolder.getInstance().getObd()
                .compose(this.<ObdData>bindToLifecycle())
                .compose(Transformers.<ObdData>observerForUI())
                .subscribe(new Action1<ObdData>() {
                    @Override
                    public void call(ObdData obdData) {
                        mObdDataHandler.handleObdData(obdData);
                    }
                });
    }

}
