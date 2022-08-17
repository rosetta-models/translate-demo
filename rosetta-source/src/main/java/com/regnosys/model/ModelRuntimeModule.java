package com.regnosys.model;

import com.google.inject.AbstractModule;
import com.regnosys.rosetta.common.hashing.ReferenceConfig;
import com.rosetta.model.lib.qualify.QualifyFunctionFactory;
import com.rosetta.model.lib.validation.ValidatorFactory;

public class ModelRuntimeModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ReferenceConfig.class).toInstance(ReferenceConfig.noScopeOrExcludedPaths());
		bind(QualifyFunctionFactory.class).to(QualifyFunctionFactory.Default.class);
		bind(ValidatorFactory.class).to(ValidatorFactory.Default.class);
	}

}
