/*
 * Copyright (C) 2020 Paul Fulham <paul@paulf.me>
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 *
 *            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 *                     Version 2, December 2004
 * 
 * Copyright (C) 2004 Sam Hocevar <sam@hocevar.net>
 * 
 * Everyone is permitted to copy and distribute verbatim or modified
 * copies of this license document, and changing it is allowed as long
 * as the name is changed.
 * 
 *            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 *   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 * 
 *  0. You just DO WHAT THE FUCK YOU WANT TO.
 */
package online.kingdomkeys.kingdomkeys.worldgen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class JigsawJank {
    private final OperatorRegistry registry;

    private JigsawJank(final OperatorRegistry registry) {
        this.registry = registry;
    }

    public JigsawJank append(final ResourceLocation name, final Supplier<List<Pair<JigsawPiece, Integer>>> additionalCategoryElements) {
        this.registry.functions.put(name, pool -> this.appendPool(pool, additionalCategoryElements.get()));
        return this;
    }

    private JigsawPattern appendPool(final JigsawPattern pool, final List<Pair<JigsawPiece, Integer>> additionalElements) {
        final ResourceLocation fallback = pool.func_214948_a();
        final ImmutableList<Pair<JigsawPiece, Integer>> elements = Objects.requireNonNull(ObfuscationReflectionHelper.getPrivateValue(JigsawPattern.class, pool, "field_214952_d"), "elements");
        final JigsawPattern.PlacementBehaviour placement = Objects.requireNonNull(ObfuscationReflectionHelper.getPrivateValue(JigsawPattern.class, pool, "field_214955_g"), "placement");
        return new JigsawPattern(pool.func_214947_b(), fallback, Stream.concat(elements.stream(), additionalElements.stream()).collect(Collectors.toList()), placement);
    }

    public static JigsawJank create() {
        final Field registryField = ObfuscationReflectionHelper.findField(JigsawManager.class, "field_214891_a");
        FieldUtils.removeFinalModifier(registryField);
        final JigsawPatternRegistry registry;
        try {
            registry = (JigsawPatternRegistry) registryField.get(null);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        final OperatorRegistry operatorRegistry = new OperatorRegistry(registry);
        try {
            registryField.set(null, operatorRegistry);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return new JigsawJank(operatorRegistry);
    }

    private static final class OperatorRegistry extends JigsawPatternRegistry {
        private final JigsawPatternRegistry delegate;

        private final Map<ResourceLocation, UnaryOperator<JigsawPattern>> functions;

        private OperatorRegistry(final JigsawPatternRegistry delegate) {
            this.delegate = delegate;
            this.functions = new HashMap<>();
        }

        @Override
        public void register(final JigsawPattern pattern) {
            if (pattern != JigsawPattern.EMPTY) {
                this.delegate.register(this.functions.getOrDefault(pattern.func_214947_b(), UnaryOperator.identity()).apply(pattern));
            }
        }

        @Override
        public JigsawPattern get(final ResourceLocation name) {
            return this.delegate.get(name);
        }
    }
}
