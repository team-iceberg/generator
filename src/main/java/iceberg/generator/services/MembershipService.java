package iceberg.generator.services;

import iceberg.generator.exceptions.ServiceException;
import iceberg.generator.models.Family;
import iceberg.generator.models.Membership;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Membership service interface
 */
public interface MembershipService {

  /**
   * Manage uploaded memberships file
   * @param file memberships file input stream
   * @return Memberships list
   * @throws ServiceException when an error occurs
   */
  List<Family> getMemberships(InputStream file) throws ServiceException;
}
