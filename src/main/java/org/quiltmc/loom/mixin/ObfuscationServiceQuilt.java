/*
 * This file is part of sponge-mixin-compile-extensions, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC, 2021 QuiltMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.quiltmc.loom.mixin;

import com.google.common.collect.ImmutableSet;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.service.IObfuscationService;
import org.spongepowered.tools.obfuscation.service.ObfuscationTypeDescriptor;

import java.util.Collection;
import java.util.Set;

public class ObfuscationServiceQuilt implements IObfuscationService {
	public static final String IN_MAP_FILE = "inMapFile";
	public static final String IN_MAP_EXTRA_FILES = "inMapExtraFiles";
	public static final String OUT_MAP_FILE = "outMapFile";
	public static final String SOURCE_NAMESPACE = "official";
	public static final String INTERMEDIATE_NAMESPACE = "hashed";
	public static final String NAMED_NAMESPACE = "named";

	private String asSuffixed(String arg, String from, String to) {
		return arg + MixinExtUtils.capitalize(from) + MixinExtUtils.capitalize(to);
	}

	private ObfuscationTypeDescriptor createObfuscationType(String from, String to) {
		return new ObfuscationTypeDescriptor(
			from + ":" + to,
			asSuffixed(ObfuscationServiceQuilt.IN_MAP_FILE, from, to),
			asSuffixed(ObfuscationServiceQuilt.IN_MAP_EXTRA_FILES, from, to),
			asSuffixed(ObfuscationServiceQuilt.OUT_MAP_FILE, from, to),
			ObfuscationEnvironmentFabric.class
		);
	}

	private void addSupportedOptions(ImmutableSet.Builder<String> builder, String from, String to) {
		builder.add(asSuffixed(ObfuscationServiceQuilt.IN_MAP_FILE, from, to));
		builder.add(asSuffixed(ObfuscationServiceQuilt.IN_MAP_EXTRA_FILES, from, to));
		builder.add(asSuffixed(ObfuscationServiceQuilt.OUT_MAP_FILE, from, to));
	}

	@Override
	public Set<String> getSupportedOptions() {
		ImmutableSet.Builder<String> builder = new ImmutableSet.Builder<>();
		addSupportedOptions(builder, SOURCE_NAMESPACE, INTERMEDIATE_NAMESPACE);
		addSupportedOptions(builder, SOURCE_NAMESPACE, NAMED_NAMESPACE);
		addSupportedOptions(builder, INTERMEDIATE_NAMESPACE, SOURCE_NAMESPACE);
		addSupportedOptions(builder, INTERMEDIATE_NAMESPACE, NAMED_NAMESPACE);
		addSupportedOptions(builder, NAMED_NAMESPACE, SOURCE_NAMESPACE);
		addSupportedOptions(builder, NAMED_NAMESPACE, INTERMEDIATE_NAMESPACE);
		return builder.build();
	}

	@Override
	public Collection<ObfuscationTypeDescriptor> getObfuscationTypes(IMixinAnnotationProcessor ap) {
		return getObfuscationTypes();
	}

	// Hook preserved for Mixin 0.7 backward compatibility
	public Collection<ObfuscationTypeDescriptor> getObfuscationTypes() {
		return ImmutableSet.of(
				createObfuscationType(SOURCE_NAMESPACE, INTERMEDIATE_NAMESPACE),
				createObfuscationType(SOURCE_NAMESPACE, NAMED_NAMESPACE),
				createObfuscationType(INTERMEDIATE_NAMESPACE, SOURCE_NAMESPACE),
				createObfuscationType(INTERMEDIATE_NAMESPACE, NAMED_NAMESPACE),
				createObfuscationType(NAMED_NAMESPACE, SOURCE_NAMESPACE),
				createObfuscationType(NAMED_NAMESPACE, INTERMEDIATE_NAMESPACE)
		);
	}
}
