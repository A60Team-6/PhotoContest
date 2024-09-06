//package com.telerikacademy.web.photocontest.services;
//
////public class PhotoServiceMentor {
////}
//@Service
//@RequiredArgsConstructor
//public class PhotoServiceImpl implements PhotoService {
//
//    private final PhotoRepository photoRepository;
//    private final ConversionService conversionService;
//    private final CloudinaryService cloudinaryService;
//    private final ContestParticipationService contestParticipationService;
//
//    @Override
//    public PhotoOutput getPhotoById(UUID id) {
//        return photoRepository.findById(id)
//                .map(photo -> conversionService.convert(photo, PhotoOutput.class))
//                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
//    }
//
//    @Override
//    public List<PhotoOutput> getAll() {
//        return photoRepository.findAllByIsActiveTrue().stream()
//                .map(photo -> conversionService.convert(photo, PhotoOutput.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public PhotoOutput getByTitle(String title) {
//        return photoRepository.findByTitleAndIsActiveTrue(title)
//                .map(photo -> conversionService.convert(photo, PhotoOutput.class))
//                .orElseThrow(() -> new EntityNotFoundException("Photo with title " + title + " not found."));
//    }
//
//    @Override
//    public List<PhotoOutput> searchByTitle(String title) {
//        return photoRepository.findByTitleContainingIgnoreCase(title).stream()
//                .map(photo -> conversionService.convert(photo, PhotoOutput.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public PhotoIdOutput createPhoto(PhotoInput photoInput, User user) {
//        validatePhotoInput(photoInput);
//        validateUserIsActive(user);
//
//        if (photoRepository.existsByTitleAndIsActiveTrue(photoInput.getTitle())) {
//            throw new IllegalArgumentException("A photo with the same title already exists.");
//        }
//
//        Contest contest = contestParticipationService.getContestById(UUID.fromString(photoInput.getContestId()));
//        validateContestPhase(contest);
//
//        ContestParticipation participation = findUserParticipation(user, contest);
//        if (participation.isPhotoUploaded()) {
//            throw new IllegalArgumentException("You have already uploaded a photo!");
//        }
//
//        Photo photo = buildAndSavePhoto(photoInput, user, contest);
//        return conversionService.convert(photo, PhotoIdOutput.class);
//    }
//
//    @Override
//    public void softDeletePhoto(UUID id) {
//        photoRepository.findById(id)
//                .map(photo -> {
//                    photo.setIsActive(false);
//                    return photoRepository.save(photo);
//                })
//                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
//    }
//
//    @Override
//    public void updatePhoto(UUID id, PhotoUpdate photoUpdate, User user) {
//        photoRepository.findById(id)
//                .map(photo -> {
//                    PermissionHelper.isSameUser(user, photo.getUser(), "This user is not the photo owner!");
//                    if (photoRepository.existsByTitleAndIsActiveTrue(photoUpdate.getTitle())) {
//                        throw new DuplicateEntityException("Photo", "title", photo.getTitle());
//                    }
//                    photo.setTitle(photoUpdate.getTitle());
//                    photo.setStory(photoUpdate.getStory());
//                    return photoRepository.save(photo);
//                })
//                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
//    }
//
//    @Override
//    public UploadFileOutput uploadPhoto(UploadFileInput uploadFileInput) throws IOException {
//        validateFileInput(uploadFileInput);
//
//        String hash = generateSHA256Hash(uploadFileInput.getFile());
//
//        if (photoRepository.existsByHash(hash)) {
//            throw new DuplicateEntityException("Duplicate photo: A photo with this content already exists.");
//        }
//
//        Photo photo = findPhotoEntityById(UUID.fromString(uploadFileInput.getPhotoId()));
//
//        String photoUrl = cloudinaryService.uploadFile(uploadFileInput.getFile());
//        if (photoUrl == null || photoUrl.isEmpty()) {
//            throw new IOException("Failed to upload photo to Cloudinary");
//        }
//
//        photo.setPhotoUrl(photoUrl);
//        photo.setHash(hash);
//
//        photoRepository.save(photo);
//
//        return UploadFileOutput.builder().message("Photo uploaded and URL updated successfully!").build();
//    }
//
//    private void validatePhotoInput(PhotoInput photoInput) {
//        if (photoInput == null) {
//            throw new IllegalArgumentException("Photo input cannot be null");
//        }
//    }
//
//    private void validateUserIsActive(User user) {
//        if (user == null || !user.getIsActive()) {
//            throw new AuthorizationException("User is not authorized or inactive");
//        }
//    }
//
//    private void validateContestPhase(Contest contest) {
//        if (!"Phase 1".equals(contest.getPhase().getName())) {
//            throw new IllegalArgumentException("You cannot upload a photo after Phase 1!");
//        }
//    }
//
//    private ContestParticipation findUserParticipation(User user, Contest contest) {
//        return contestParticipationService.getAll().stream()
//                .filter(p -> p.getContest().equals(contest) && p.getUser().equals(user))
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("No valid contest participation found for the user."));
//    }
//
//    private Photo buildAndSavePhoto(PhotoInput photoInput, User user, Contest contest) {
//        Photo photo = Photo.builder()
//                .title(photoInput.getTitle())
//                .story(photoInput.getStory())
//                .contest(contest)
//                .user(user)
//                .build();
//
//        user.getPhotos().add(photo);
//        return photoRepository.save(photo);
//    }
//
//    private void validateFileInput(UploadFileInput uploadFileInput) {
//        if (uploadFileInput == null || uploadFileInput.getFile() == null || uploadFileInput.getFile().isEmpty()) {
//            throw new IllegalArgumentException("Invalid file input");
//        }
//    }
//
//    private String generateSHA256Hash(MultipartFile file) throws IOException {
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hashBytes = digest.digest(file.getBytes());
//            return convertToHexString(hashBytes);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Could not initialize SHA-256 algorithm", e);
//        }
//    }
//
//    private String convertToHexString(byte[] hashBytes) {
//        return String.format("%064x", new java.math.BigInteger(1, hashBytes));
//    }
//
//    @Override
//    public Photo findPhotoEntityById(UUID id) {
//        return photoRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
//    }
//}
