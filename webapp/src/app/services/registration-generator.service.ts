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
        this.http.post<any>('generator/membership/file', formData).subscribe(data => {
                const blob = new Blob([data], {
                    type: 'application/zip'
                });
                const url = window.URL.createObjectURL(blob);
                window.open(url);
                this.toastService.info('La génération des fiches en cours');
            },
            (err: HttpErrorResponse) => {
                this.toastService.alert('Une erreur est survenue lors de la génération des fiches');
            });
    }
}
