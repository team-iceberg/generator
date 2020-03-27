import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {ToastService} from '../shared/toast/toast.service';

@Injectable()
export class RegistrationGeneratorService {

    constructor(private http: HttpClient, private toastService: ToastService) {
    }

    generateRegistration(file: File) {
        const formData = new FormData();
        formData.append('file', file, file.name);
        this.http.post<void>('generator/membership/file', formData).subscribe(value => {
                this.toastService.info('La génération des fiches en cours');
            },
            (err: HttpErrorResponse) => {
                this.toastService.alert('Une erreur est survenue lors de la génération des fiches');
            });
    }
}
