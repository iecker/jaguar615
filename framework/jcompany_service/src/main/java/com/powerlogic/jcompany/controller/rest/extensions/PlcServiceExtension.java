package com.powerlogic.jcompany.controller.rest.extensions;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.util.AnnotationLiteral;

import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;
import com.powerlogic.jcompany.controller.rest.api.conversor.IPlcConversor;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;

public class PlcServiceExtension implements Extension {

	void afterDeploymentValidation(@Observes AfterDeploymentValidation deploymentValidation, BeanManager beanManager, PlcServiceManager serviceManager) {

		final AnnotationLiteral<Any> anyQualifier = new AnnotationLiteral<Any>() {
			private static final long serialVersionUID = 1L;
		};

		Set<Bean<?>> allControllers = beanManager.getBeans(IPlcController.class, anyQualifier);

		for (Bean<?> bean : allControllers) {
			if (bean.getBeanClass().isAnnotationPresent(SPlcController.class)) {
				try {
					serviceManager.addController(bean);
				} catch (Throwable e) {
					deploymentValidation.addDeploymentProblem(e);
				}
			}
		}

		Set<Bean<?>> allConversors = beanManager.getBeans(IPlcConversor.class, anyQualifier);

		for (Bean<?> bean : allConversors) {
			if (bean.getBeanClass().isAnnotationPresent(SPlcConversor.class)) {
				try {
					serviceManager.addConversor(bean);
				} catch (Throwable e) {
					deploymentValidation.addDeploymentProblem(e);
				}
			}
		}
	}

	public static <T extends Annotation> T findQualifier(Set<Annotation> qualifiers, Class<T> type) {
		for (Annotation annotation : qualifiers) {
			if (annotation.annotationType().equals(type)) {
				return type.cast(annotation);
			}
		}
		return null;
	}
}
