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
import com.nonda.dtc.ui.obd.RangeViewHolder;
import com.nonda.dtc.views.NumberAnimTextView;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by whaley on 2017/5/29.
 */

public class EcoFragment extends BaseFragment {

    private ObdDataHandler mObdDataHandler;

    @BindView(R.id.instantMpg)
    TextView instantMpg;

    @BindView(R.id.averageMpg)
    NumberAnimTextView averageMpg;

    @BindView(R.id.fuleLevel)
    TextView fuleLevel;

    @BindView(R.id.icon_fuel_level)
    ImageView iconFuelLevel;

    @BindView(R.id.lable_fuel_level)
    TextView labelFuelLevel;

    @BindView(R.id.fuel_unit)
    TextView fuelUnit;

    @BindView(R.id.range)
    TextView range;

    @BindView(R.id.icon_range)
    ImageView iconRange;

    @BindView(R.id.label_range)
    TextView labelRange;

    @BindView(R.id.range_unit)
    TextView rangeUnit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = createFragmentView(inflater, container, R.layout.fragment_eco, savedInstanceState);
        initViews();
        return view;
    }

    private void initViews() {
        mObdDataHandler = new ObdDataHandler(new ObdViewHolder() {

            @Override
            public FuelViewHolder getFuelViewHolder() {
                return new FuelViewHolder() {
                    @Override
                    public TextView getFuelLevel() {
                        return fuleLevel;
                    }

                    @Override
                    public ImageView getFuelLevelIcon() {
                        return iconFuelLevel;
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
            public TextView getInstantMpg() {
                return instantMpg;
            }

            @Override
            public NumberAnimTextView getAverageMpg() {
                return averageMpg;
            }

            @Override
            public RangeViewHolder getRangeViewHolder() {
                return new RangeViewHolder() {
                    @Override
                    public TextView getRange() {
                        return range;
                    }

                    @Override
                    public ImageView getRangeIcon() {
                        return iconRange;
                    }

                    @Override
                    public TextView getRangeLabel() {
                        return labelRange;
                    }

                    @Override
                    public TextView getRangeUnit() {
                        return rangeUnit;
                    }
                };
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
