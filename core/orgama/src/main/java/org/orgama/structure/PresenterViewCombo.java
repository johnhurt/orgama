package org.orgama.structure;

/**
 * storage class representing a matched pair of presenter and view
 * @author kguthrie
 */
public class PresenterViewCombo {

	private final PresenterRef presenter;
	private final ViewRef view;
	
	public PresenterViewCombo(PresenterRef presenter, 
			ViewRef view) {
		this.presenter = presenter;
		this.view = view;
	}

	/**
	 * @return the presenter
	 */
	public PresenterRef getPresenter() {
		return presenter;
	}

	/**
	 * @return the view
	 */
	public ViewRef getView() {
		return view;
	}

	/**
	 * @return the nameToken
	 */
	public String getNameToken() {
		return presenter.getNameToken();
	}

	/**
	 * @return the disableCodeSplitting
	 */
	public boolean isDisableCodeSplitting() {
		return presenter.isDisableCodeSplitting();
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		presenter.setProxy(new ProxyRef(presenter, uri));
	}
	
	
}
