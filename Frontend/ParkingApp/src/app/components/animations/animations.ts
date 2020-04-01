import {animate, animateChild, group, query as q, sequence, style, transition, trigger} from '@angular/animations';


const query = (s, a, o = {optional: true}) => q(s, a, o);

export const routerTransition = trigger('routerTransition', [
    transition('* => *', [
        query(':enter, :leave', style({position: 'fixed', width: '100%'})),
        query(':enter', style({transform: 'translateX(100%)'})),
        sequence([
            query(':leave', animateChild()),
            group([
                query(':leave', [
                    style({transform: 'translateX(0%)'}),
                    animate(
                        '500ms cubic-bezier(.75,-0.48,.26,1.52)',
                        style({transform: 'translateX(-100%)'})
                    )
                ]),
                query(':enter', [
                    style({transform: 'translateX(100%)'}),
                    animate(
                        '500ms cubic-bezier(.75,-0.48,.26,1.52)',
                        style({transform: 'translateX(0%)'})
                    )
                ])
            ]),
            query(':enter', animateChild())
        ])
    ])
]);

export const enterAnimation = trigger('enterAnimation', [
    transition(':enter', [
        style({transform: 'translateY(100%)', opacity: 0}),
        animate('500ms', style({transform: 'translateX(0)', opacity: 1}))
    ]),
    transition(':leave', [
        style({transform: 'translateY(0)', opacity: 1}),
        animate('500ms', style({transform: 'translateX(100%)', opacity: 0}))
    ])
]);

export const fadeInOut = trigger('fadeInOut', [
    transition(':enter', [
        // :enter is alias to 'void => *'
        style({opacity: 0}),
        animate(500, style({opacity: 1}))
    ]),
    transition(':leave', [
        // :leave is alias to '* => void'
        animate(500, style({opacity: 0}))
    ])
]);

export const openClose = trigger('openClose', [
    transition('* => *', [animate('1s')]),
    transition('* => *', [animate('0.5s')])
]);
