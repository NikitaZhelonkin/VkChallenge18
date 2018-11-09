package com.vk.challenge.di;

import com.vk.challenge.data.repository.FeedRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    FeedRepository feedrepository();
}
