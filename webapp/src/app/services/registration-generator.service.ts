import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';

@Injectable()
export class RegistrationGeneratorService {

    constructor(private http: HttpClient) {
    }

    generateRegistration(file: File) {
        const formData = new FormData();
        formData.append('file', file, file.name);
        this.http.get<void>('generator/membership').subscribe();
    }
}
