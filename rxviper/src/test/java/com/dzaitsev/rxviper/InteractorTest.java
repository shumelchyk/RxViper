/*
 * Copyright 2016 Dmytro Zaitsev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dzaitsev.rxviper;

import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static rx.Observable.just;

/**
 * ~ ~ ~ ~ Description ~ ~ ~ ~
 *
 * @author Dmytro Zaitsev
 * @since 2016-May-14, 13:56
 */
public final class InteractorTest {
  private static final int                PARAM        = 1;
  private static final Action1<String>    ON_NEXT      = Actions.empty();
  private static final Action1<Throwable> ON_ERROR     = Actions.empty();
  private static final Action0            ON_COMPLETED = Actions.empty();
  private static final Subscriber<String> SUBSCRIBER   = new ActionSubscriber<>(ON_NEXT, ON_ERROR, ON_COMPLETED);

  private Interactor<Integer, String> spyInteractor;

  @Before
  public void setUp() {
    spyInteractor = spy(new Interactor<Integer, String>(Schedulers.immediate(), Schedulers.immediate()) {
      @Override
      protected Observable<String> createObservable(final Integer integer) {
        return just(String.valueOf(integer));
      }
    });
  }

  @Test
  public void shouldCreateObservableSubscriber() {
    spyInteractor.execute(SUBSCRIBER);
    verify(spyInteractor).createObservable(null);
  }

  @Test
  public void shouldCreateObservableSubscriberParam() {
    spyInteractor.execute(SUBSCRIBER, PARAM);
    verify(spyInteractor).createObservable(PARAM);
  }

  @Test
  public void shouldCreateObservableOnNext() {
    spyInteractor.execute(ON_NEXT);
    verify(spyInteractor).createObservable(null);
  }

  @Test
  public void shouldCreateObservableOnNextParam() {
    spyInteractor.execute(ON_NEXT, PARAM);
    verify(spyInteractor).createObservable(PARAM);
  }

  @Test
  public void shouldCreateObservableOnNextOnError() {
    spyInteractor.execute(ON_NEXT, ON_ERROR);
    verify(spyInteractor).createObservable(null);
  }

  @Test
  public void shouldCreateObservableOnNextOnErrorOnComplete() {
    spyInteractor.execute(ON_NEXT, ON_ERROR, ON_COMPLETED);
    verify(spyInteractor).createObservable(null);
  }

  @Test
  public void shouldCreateObservableOnNextOnErrorParam() {
    spyInteractor.execute(ON_NEXT, ON_ERROR, PARAM);
    verify(spyInteractor).createObservable(PARAM);
  }

  @Test
  public void shouldCreateObservableOnNextOnErrorOnCompleteParam() {
    spyInteractor.execute(ON_NEXT, ON_ERROR, ON_COMPLETED, PARAM);
    verify(spyInteractor).createObservable(PARAM);
  }

  @Test
  public void shouldBeUnSubscribedOnStart() {
    assertThat(spyInteractor.isUnsubscribed()).isTrue();
  }

  @Test
  public void shouldUnsubscribe() {
    spyInteractor.unsubscribe();
    assertThat(spyInteractor.isUnsubscribed()).isTrue();
    spyInteractor.unsubscribe();
    assertThat(spyInteractor.isUnsubscribed()).isTrue();
  }

  @Test(expected = IllegalArgumentException.class)
  public void subscriberShouldNotBeNull() {
    //noinspection unchecked
    spyInteractor.execute((Subscriber) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void subscriberParamShouldNotBeNull() {
    //noinspection unchecked
    spyInteractor.execute((Subscriber) null, PARAM);
  }

  @Test(expected = IllegalArgumentException.class)
  public void onNextShouldNotBeNull() {
    //noinspection unchecked
    spyInteractor.execute((Action1) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void onNextParamShouldNotBeNull() {
    //noinspection unchecked
    spyInteractor.execute((Action1) null, PARAM);
  }

  @Test(expected = IllegalArgumentException.class)
  public void onErrorShouldNotBeNull() {
    //noinspection unchecked
    spyInteractor.execute(ON_NEXT, (Action1) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void onErrorParamShouldNotBeNull() {
    spyInteractor.execute(ON_NEXT, null, PARAM);
  }

  @Test(expected = IllegalArgumentException.class)
  public void onCompleteShouldNotBeNull() {
    spyInteractor.execute(ON_NEXT, ON_ERROR, (Action0) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void onCompleteParamShouldNotBeNull() {
    spyInteractor.execute(ON_NEXT, ON_ERROR, null, PARAM);
  }

  @Test(expected = IllegalArgumentException.class)
  public void subscribeOnShouldNotBeNull() {
    new Interactor<Object, Object>(null, Schedulers.immediate()) {
      @Override
      protected Observable<Object> createObservable(final Object o) {
        return null;
      }
    };
  }

  @Test(expected = IllegalArgumentException.class)
  public void observeOnOnShouldNotBeNull() {
    new Interactor<Object, Object>(null, Schedulers.immediate()) {
      @Override
      protected Observable<Object> createObservable(final Object o) {
        return null;
      }
    };
  }
}