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
package online.kingdomkeys.kingdomkeys.world.features;

//Probably won't need this anymore and if we do reflection fields will need to change
/*
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
        final ResourceLocation fallback = pool.getFallback();
        final ImmutableList<Pair<JigsawPiece, Integer>> elements = Objects.requireNonNull(ObfuscationReflectionHelper.getPrivateValue(JigsawPattern.class, pool, "field_214952_d"), "elements");
        final JigsawPattern.PlacementBehaviour placement = Objects.requireNonNull(ObfuscationReflectionHelper.getPrivateValue(JigsawPattern.class, pool, "field_214955_g"), "placement");
        return new JigsawPattern(pool.getName(), fallback, Stream.concat(elements.stream(), additionalElements.stream()).collect(Collectors.toList()), placement);
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
                this.delegate.register(this.functions.getOrDefault(pattern.getName(), UnaryOperator.identity()).apply(pattern));
            }
        }

        @Override
        public JigsawPattern get(final ResourceLocation name) {
            return this.delegate.get(name);
        }
    }
}
*/