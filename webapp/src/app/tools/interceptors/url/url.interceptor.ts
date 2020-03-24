import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';

import {environment} from '../../../../environments/environment';
import {SpinnerStore} from '../../stores/spinner/spinner.store';

@Injectable()
export class UrlInterceptor implements HttpInterceptor {

    constructor(private spinnerStore: SpinnerStore) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        this.spinnerStore.activate();
        let url: string;
        if (req.url.indexOf('i18n') === -1) {
            url = `${environment.BACKEND_BASE_URL}${req.url}`;
        } else {
            url = `${environment.BASE_URL}${req.url}`;
        }
        const newRequest = req.clone({url});
        return next.handle(newRequest).pipe(
            tap(evt => {
                this.spinnerStore.deactivate()
            }, (err: any) => {
                console.log(err);
                this.spinnerStore.deactivate();
            })
        );
    }
}
