package org.orgama.structure;

import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

/**
 *
 * @author kguthrie
 */
public class ProxyRef extends ClassWrapperRef {

	private final PresenterRef presenter;
	private String uri;
	private final Class<?> proxyAnnotationBase;

	public ProxyRef(PresenterRef presenter, String uri) {
		super((uri == null)?(Proxy.class):(ProxyPlace.class));
		this.presenter = presenter;
		proxyAnnotationBase = (presenter.isDisableCodeSplitting())
				?(ProxyStandard.class)
				:(ProxyCodeSplit.class);
		this.uri = uri;
	}

	@Override
	public String generateDefinition() {
		StringBuilder result = new StringBuilder();
		
		result.append(generateProxyAnnotations());
		
		result.append("\n");
		
		result.append(
			String.format(
					"public interface %s extends %s<%s>{}", 
					getGenClassName(),
					getOriginalSimpleName(),
					getPresenter().getGenClassName()));
		
		return result.toString();
	}
	
	/**
	 * generate the proxy annotations for the presenter view combo given
	 * @param pvc
	 * @return 
	 */
	private String generateProxyAnnotations() {
		StringBuilder result = new StringBuilder();
		
		if (uri != null) {
			result.append(String.format("@NameToken(\"%s\")\n", uri));
		}
		
		result.append("@");
		result.append(getProxyAnnotationBase().getSimpleName());
		
		return result.toString();
	}
	
	@Override
	public String getGenClassName() {
		return "Prox";
	}

	
	/**
	 * @return the presenter
	 */
	public PresenterRef getPresenter() {
		return presenter;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @return the proxyAnnotationBase
	 */
	public Class<?> getProxyAnnotationBase() {
		return proxyAnnotationBase;
	}

}

