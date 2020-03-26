import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AppRoutingModule} from './app-routing.module';

import {AppComponent} from './app.component';
import {HomeModule} from './home/home.module';
import {SharedModule} from './shared/shared.module';
import {ToastService} from './shared/toast/toast.service';
import {UrlInterceptor} from './tools/interceptors/url/url.interceptor';
import {SpinnerStore} from './tools/stores/spinner/spinner.store';

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        AppRoutingModule,
        HomeModule,
        HttpClientModule,
        BrowserModule,
        BrowserAnimationsModule,
        SharedModule
    ],
    providers: [
        SpinnerStore,
        ToastService,
        {provide: HTTP_INTERCEPTORS, useClass: UrlInterceptor, multi: true}
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
