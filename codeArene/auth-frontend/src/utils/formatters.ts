export const formatDate = (dateString: string | undefined): string => {
  if (!dateString) return 'Never';

  try {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date);
  } catch (error) {
    return 'Invalid date';
  }
};

export const formatRelativeTime = (dateString: string | undefined): string => {
  if (!dateString) return 'Never';

  try {
    const date = new Date(dateString);
    const now = new Date();
    const diffInMs = now.getTime() - date.getTime();

    const diffInSeconds = Math.floor(diffInMs / 1000);
    const diffInMinutes = Math.floor(diffInSeconds / 60);
    const diffInHours = Math.floor(diffInMinutes / 60);
    const diffInDays = Math.floor(diffInHours / 24);

    if (diffInSeconds < 60) {
      return 'Just now';
    } else if (diffInMinutes < 60) {
      return `${diffInMinutes} minute${diffInMinutes === 1 ? '' : 's'} ago`;
    } else if (diffInHours < 24) {
      return `${diffInHours} hour${diffInHours === 1 ? '' : 's'} ago`;
    } else if (diffInDays < 7) {
      return `${diffInDays} day${diffInDays === 1 ? '' : 's'} ago`;
    } else {
      return formatDate(dateString);
    }
  } catch (error) {
    return 'Invalid date';
  }
};

export const formatUserRole = (role: string): string => {
  switch (role) {
    case 'ADMIN':
      return 'Administrator';
    case 'MODERATOR':
      return 'Moderator';
    case 'USER':
      return 'User';
    default:
      return role;
  }
};

export const getRoleBadgeColor = (role: string): string => {
  switch (role) {
    case 'ADMIN':
      return 'bg-red-100 text-red-800';
    case 'MODERATOR':
      return 'bg-yellow-100 text-yellow-800';
    case 'USER':
      return 'bg-green-100 text-green-800';
    default:
      return 'bg-gray-100 text-gray-800';
  }
};

export const getStatusBadgeColor = (isEnabled: boolean, isLocked: boolean): string => {
  if (!isEnabled) {
    return 'bg-red-100 text-red-800';
  } else if (isLocked) {
    return 'bg-orange-100 text-orange-800';
  } else {
    return 'bg-green-100 text-green-800';
  }
};

export const getStatusText = (isEnabled: boolean, isLocked: boolean): string => {
  if (!isEnabled) {
    return 'Disabled';
  } else if (isLocked) {
    return 'Locked';
  } else {
    return 'Active';
  }
};

export const truncateText = (text: string, maxLength: number): string => {
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
};

export const capitalizeFirst = (text: string): string => {
  return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
};

export const getInitials = (firstName: string, lastName: string): string => {
  return `${firstName.charAt(0).toUpperCase()}${lastName.charAt(0).toUpperCase()}`;
};

export const formatNumber = (num: number): string => {
  return new Intl.NumberFormat('en-US').format(num);
};

export const pluralize = (count: number, singular: string, plural?: string): string => {
  if (count === 1) return singular;
  return plural || `${singular}s`;
};
