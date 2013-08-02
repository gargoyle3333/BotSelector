package net.bot.gui;

public abstract class BaseScreen {
	
	protected MasterScreen mMasterScreen;
	
	public BaseScreen(MasterScreen master) {
		mMasterScreen = master;
	}

	public abstract String getTitle();

}
