import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders} from '@angular/common/http';
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
        let apiReq: HttpRequest<any> = req.clone();
        if (req.url.indexOf('i18n') == -1) {
            if (req.url.toString().indexOf("file") > -1) {
                apiReq = req.clone({url: `${environment.BACKEND_BASE_URL}${req.url}`, responseType: 'blob'});
            } else {
                apiReq = req.clone({url: `${environment.BACKEND_BASE_URL}${req.url}`});
            }
        } else {
            apiReq = req.clone({url: `${environment.BASE_URL}${req.url}`});
        }

        return next.handle(apiReq).pipe(
            tap(evt => {
                this.spinnerStore.deactivate()
            }, (err: any) => {
                console.log(err);
                this.spinnerStore.deactivate();
            })
        );
    }
}
