import { hosts, ports } from './ports';


export const environment = {
    production: true,
    frontUrl: hosts.protocol + hosts.digitalOcean + ':' + ports.front,
    restUrl: hosts.protocol + hosts.digitalOcean + ':' + ports.rest
};
