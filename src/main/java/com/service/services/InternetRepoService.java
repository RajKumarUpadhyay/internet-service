package com.service.services;

import com.service.entities.Product;
import com.service.entities.UserProfile;
import com.service.exception.IdStorageException;
import com.service.exception.ResourceNotFoundException;
import com.service.exception.UserIdNotFoundException;
import com.service.property.FileStorageProperties;
import com.service.repositories.ProductRepository;
import com.service.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class InternetRepoService {

    private Path fileStorageLocation;
    private static final String PENDING = "PENDING";
    private static final String CANCELED = "CANCELED";

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserProfileRepository userProfileRepository;

    public void internetRequestService(UserProfile userProfile) {

        if (Objects.isNull(userProfile.getProduct()))
            throw new ResourceNotFoundException("Service Product is missing in the request");

        try {
            Product product = productRepository.findProductByProductType(userProfile.getProduct().getProductType());
            if (Objects.isNull(product)) {
                productRepository.save(userProfile.getProduct());
            } else {
                userProfile.setProduct(product);
            }
            userProfile.setSubscription(true);
            userProfile.setOrderStatus(PENDING);
            userProfileRepository.save(userProfile);
        }
        catch (Exception e)
        {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public UserProfile cancelInternetSubscriptionRequest(Long user_id) {

        UserProfile userProfile = userProfileRepository.findUserProfileById(user_id);

        if (Objects.nonNull(userProfile)) {
            userProfile.setSubscription(false);
            userProfile.setOrderStatus(CANCELED);
            userProfileRepository.save(userProfile);
        } else {
            throw new ResourceNotFoundException("User ID "+user_id +" not exist");
        }
        return userProfile;
    }

    public UserProfile updateProfileIdRequest(Long user_id , File profile_id) {

        UserProfile userProfile = userProfileRepository.findUserProfileById(user_id);

        if (Objects.isNull(userProfile))
            throw new ResourceNotFoundException("Request User id "+user_id+" doesn't exist");
        String fileName = storeFile(profile_id);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/profileIDs/")
                .path(fileName)
                .toUriString();

        userProfile.setUser_pic_id(fileDownloadUri);
        userProfileRepository.save(userProfile);

        return userProfile;
    }

    public List<UserProfile> getAllInternetSubscriptionRequest() {
        List<UserProfile> profileList = userProfileRepository.findAll();

        if (profileList.size() == 0)
            throw new ResourceNotFoundException("No request order present");
        return profileList;
    }

    @Autowired
    public InternetRepoService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new IdStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(File file) {
        String fileName = StringUtils.cleanPath(file.getName());
        try {

            if(fileName.contains("..")) {
                throw new IdStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(Paths.get(file.getPath()), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IdStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new UserIdNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new UserIdNotFoundException("File not found " + fileName, ex);
        }
    }
}
