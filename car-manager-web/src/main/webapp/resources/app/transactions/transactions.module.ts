import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';

import { TransactionCreateComponent } from './component/transaction-create.component';
import { TransactionListComponent } from './component/transaction-list.component';

import { TransactionsRoutingModule } from './transactions-routing.module';

import { TransactionService } from '../core/service/transaction.service';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        TransactionsRoutingModule
    ],
    declarations: [
        TransactionCreateComponent,
        TransactionListComponent
    ],
    exports: [
        TransactionCreateComponent,
        TransactionListComponent
    ],
    providers: [
        TransactionService
    ]
})
export class TransactionsModule {
    
}