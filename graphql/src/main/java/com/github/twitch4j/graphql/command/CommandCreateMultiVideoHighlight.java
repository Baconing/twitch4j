package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.github.twitch4j.graphql.internal.CreateMultiVideoHighlightMutation;
import com.github.twitch4j.graphql.internal.type.CreateMultiVideoHighlightInput;
import org.jetbrains.annotations.NotNull;

public class CommandCreateMultiVideoHighlight extends BaseCommand<CreateMultiVideoHighlightMutation.Data> {
    private final CreateMultiVideoHighlightInput input;

    public CommandCreateMultiVideoHighlight(@NotNull ApolloClient apolloClient, @NotNull CreateMultiVideoHighlightInput input) {
        super(apolloClient);
        this.input = input;
    }

    @Override
    protected ApolloCall<CreateMultiVideoHighlightMutation.Data> getGraphQLCall() {
        return apolloClient.mutate(
            CreateMultiVideoHighlightMutation.builder()
                .input(input)
                .build()
        );
    }
}
