/**
 * Parking lot statuses
 */
export const parkingStatuses = {
    FREE: 'FREE',
    OCCUPIED: 'OCCUPIED',
    UNKNOWN: 'UNKNOWN',
    RESERVED: 'RESERVED'
};

/**
 * Parking lot statuses colors
 */
export const parkingColors = [
    { status: parkingStatuses.FREE, background: '#28a745' },
    { status: parkingStatuses.OCCUPIED, background: '#dc3545' },
    { status: parkingStatuses.UNKNOWN, background: 'gray' },
    { status: parkingStatuses.RESERVED, background: '#ffbf0f' }
];
