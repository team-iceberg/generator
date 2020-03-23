import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';

@Injectable()
export class RegistrationGeneratorService {

    constructor(private http: HttpClient) {
    }

    generateRegistration(file: File) {
        const formData = new FormData();
        formData.append('file', file, file.name);
        this.http.post<void>('generator/membership', formData)
            .toPromise()
            .catch((err: HttpErrorResponse) => {
                console.log(err);
            });
    }
}
