package com.example.finalproject.runmodule;

public class StepListener implements IStepListener {
	
	private Listener mListener;
	private int steps = 0;

	public StepListener(Listener listener) {
		mListener = listener;
	}
	
	public interface Listener {
		public void valueChanged(int value);
		public void passValue();
	}
	
	void notifyListener() {
		mListener.valueChanged(steps);
	}

	@Override
	public void onStep() {
		steps++;
		notifyListener();
	}

	@Override
	public void passValue() {
		// TODO Auto-generated method stub

	}
	
	public void setStep(int step) {
		this.steps = step;
		notifyListener();
	}
	
	
}
